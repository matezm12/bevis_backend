package com.bevis.assetimport.codereadrservices;

import com.bevis.assetimport.AssetImportException;
import com.bevis.assetimport.BaseImportService;
import com.bevis.assetimport.CodeReadrImportEventHandler;
import com.bevis.assetimport.domain.AssetImport;
import com.bevis.assetimport.domain.CodeReadrService;
import com.bevis.assetimport.domain.enumeration.CodeReadrServiceType;
import com.bevis.assetimport.dto.AssetImportDTO;
import com.bevis.assetimport.dto.ImportWrappingDTO;
import com.bevis.assetimport.dto.ProductDTO;
import com.bevis.assetimport.mapper.CodeReadrQuestionsMapper;
import com.bevis.assetimport.mapper.DynamicAssetImportMapper;
import com.bevis.assetimport.repository.AssetImportRepository;
import com.bevis.assettype.domain.AssetType;
import com.bevis.common.exception.DuplicateObjectException;
import com.bevis.asset.DynamicAssetException;
import com.bevis.asset.DynamicAssetService;
import com.bevis.asset.dto.AssetRequest;
import com.bevis.asset.dto.FieldValueDTO;
import com.bevis.master.MasterGeneratingService;
import com.bevis.master.MasterPermissionCheckingService;
import com.bevis.master.MasterService;
import com.bevis.master.PermissionDeniedException;
import com.bevis.master.domain.Master;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bevis.assetimport.util.BarcodeUtil.getAssetsFromBarcode;

@Service
@Slf4j
@Transactional
class DynamicImportServiceImpl implements BaseImportService {

    public static final CodeReadrServiceType SERVICE_KEY = CodeReadrServiceType.DYNAMIC_IMPORT_SERVICE;

    private final static String ASSETS_TABLE_PREFIX = "assets_";
    private final static String ASSET_ID_FIELD_SUFFIX = "_asset_id";

    private final MasterService masterService;
    private final DynamicAssetService dynamicAssetService;
    private final DynamicAssetImportMapper dynamicAssetImportMapper;
    private final CodeReadrQuestionsMapper codeReadrQuestionsMapper;
    private final AssetImportRepository assetImportRepository;
    private final MasterGeneratingService masterGeneratingService;
    private final CodeReadrImportEventHandler codeReadrImportEventHandler;
    private final MasterPermissionCheckingService masterPermissionCheckingService;
    private final AssetImportNotificationService assetImportNotificationService;

    private final AssetImportContextLoader assetImportContextLoader;

    protected DynamicImportServiceImpl(CodeReadrContextService codeReadrContextService, AssetImportContextLoader assetImportContextLoader) {
        this.masterService = codeReadrContextService.getMasterService();
        this.assetImportRepository = codeReadrContextService.getAssetImportRepository();
        this.dynamicAssetService = codeReadrContextService.getDynamicAssetService();
        this.dynamicAssetImportMapper = codeReadrContextService.getDynamicAssetImportMapper();
        this.codeReadrQuestionsMapper = codeReadrContextService.getCodeReadrQuestionsMapper();
        this.masterGeneratingService = codeReadrContextService.getMasterGeneratingService();
        this.codeReadrImportEventHandler = codeReadrContextService.getCodeReadrImportEventHandler();
        this.masterPermissionCheckingService = codeReadrContextService.getMasterPermissionCheckingService();
        this.assetImportContextLoader = assetImportContextLoader;
        this.assetImportNotificationService = codeReadrContextService.getAssetImportNotificationService();
    }

    @Override
    public CodeReadrServiceType getServiceKey() {
        return SERVICE_KEY;
    }

    @Override
    public AssetImport importAssets(AssetImportDTO assetImportDTO) {
        ImportWrappingDTO importWrappingDTO = ImportWrappingDTO.of(assetImportDTO);
        importWrappingDTO.setCodeReadrService(assetImportContextLoader.fetchCodeReadrService(assetImportDTO.getServiceId()));
        AssetImport assetImport = createOrGetAssetImportByScanId(assetImportDTO.getScanId());
        try {
            importBasicData(assetImportDTO, assetImport);
            importWrappingDTO.setOperatorAssetId(assetImportContextLoader.fetchOperatorAssetId(importWrappingDTO));

            String vendorAssetId = assetImportContextLoader.loadVendorAssetId(importWrappingDTO);
            assetImport.setUsername(assetImportDTO.getUsername());
            assetImport.setVendorAssetId(vendorAssetId);
            importWrappingDTO.setVendorAssetId(vendorAssetId);

            importWrappingDTO.setProductDTO(assetImportContextLoader.fetchProduct(assetImport));
            importWrappingDTO.setProductMaster(assetImportContextLoader.getProductMaster(importWrappingDTO.getProductDTO())
                    .orElse(null));

            importWrappingDTO.setAssetType(loadAssetType(assetImport, importWrappingDTO));

            assetImport.setSku(importWrappingDTO.getProductMaster());

            preImport(importWrappingDTO);//override in dependent classes
            final List<Master> masters = createOrFetchMasters(assetImportDTO);
            if (!isReimportAllowed(importWrappingDTO)) {
                validateMasters(masters, assetImportDTO.getScanId());
            } else {
                log.debug("validateMasters turned off");
            }
            if (masters.size() > 0) {
                importWrappingDTO.setMasters(masters);
                assetImport.setAssetsCount((long) masters.size());
                masterService.saveAll(importMasters(masters, importWrappingDTO));
                log.debug("Saved all masters");
            } else {
                assetImport.setMatched(false);
                log.error("Scan id: {} don't find any master records", assetImportDTO.getScanId());
                throw new Exception("Scan id: " + assetImportDTO.getScanId() + " don't find any master records");
            }
            assetImportNotificationService.notifyImportCreated(importWrappingDTO);
        } catch (Exception e) {
            assetImport.setMatched(false);
            assetImport.setError(e.getMessage());
            assetImportNotificationService.notifyImportFailed(importWrappingDTO.getDynamicAssetImport(), assetImport.getError());
            log.error("Error import scan: {}, with error: {}", assetImportDTO.getScanId(), e.getMessage());
        }
        log.debug("Saving asset import....");
        assetImportRepository.saveAndFlush(assetImport);
        if (importWrappingDTO.getCodeReadrService().isNotify()) {
            codeReadrImportEventHandler.onAssetImportPrepared(importWrappingDTO);
        }
        return assetImport;
    }

    @Override
    public AssetType loadAssetType(AssetImport assetImport, ImportWrappingDTO assetImportDTO) throws AssetImportException {
        CodeReadrService codeReadrService = assetImportDTO.getCodeReadrService();
        String assetsTable = codeReadrService.getAssetsTable();
        if (Strings.isNullOrEmpty(assetsTable)) {
            return null;
        }
        String ASSETS_KEY = "assets";
        if (ASSETS_KEY.equals(assetsTable)) {
            ProductDTO productDTO = assetImportDTO.getProductDTO();
            if (Objects.isNull(productDTO)) {
                throw new AssetImportException("ScanId: " + assetImport.getScanId() + ", can not find Product for this scan");
            }
            return assetImportContextLoader.loadAssetTypeByProduct(productDTO);
        } else {
            return assetImportContextLoader.loadAssetTypeByKey(assetsTable);
        }
    }

    @Override
    public AssetRequest getProductRequestFromImport(String assetId, ImportWrappingDTO assetImportDTO) {
        AssetRequest assetRequest = new AssetRequest();
        assetRequest.setAssetId(assetId);
        Map<String, FieldValueDTO> fieldValues = new HashMap<>();
        fieldValues.putAll(getDynamicQuestionFieldValues(assetImportDTO.getDynamicAssetImport()));
        fieldValues.putAll(getStaticFieldValues(assetImportDTO));
        assetRequest.setFieldValues(fieldValues);
        return assetRequest;
    }

    protected void preImport(ImportWrappingDTO assetImportDTO) throws DynamicAssetException {
        log.debug("Codereadr: preImport");
        final String assetKey = assetImportDTO.getCodeReadrService().getParentAssetKey();
        if (Objects.nonNull(assetKey)) {
            final String parentAssetIdOrPublicKey = assetImportDTO.getDynamicAssetImport()
                    .getQuestion(assetKey + "AssetId");
            final String parentAssetId = codeReadrQuestionsMapper.getAssetId(parentAssetIdOrPublicKey);
            if (Objects.nonNull(parentAssetId)) {
                AssetType assetType = assetImportContextLoader.loadAssetTypeByKey(ASSETS_TABLE_PREFIX + assetKey);
                log.trace(assetType.toString());
                AssetRequest parentAssetRequest = getProductRequestFromImport(parentAssetId, assetImportDTO);
                String fieldToRemove = assetKey + ASSET_ID_FIELD_SUFFIX; //remove this assetId from dynamic fields
                parentAssetRequest.getFieldValues().remove(fieldToRemove);
                log.trace(parentAssetRequest.toString());
                parentAssetRequest.setAssetTypeId(assetType.getId());
                dynamicAssetService.create(parentAssetRequest);
                log.debug("New asset created");
            }
        }
    }

    protected void createDynamicAsset(Master master, AssetRequest productRequestFromImport) throws DynamicAssetException {
        AssetType assetType = master.getAssetType();
        Long assetTypeId = null;
        if (Objects.nonNull(assetType)) {
            assetTypeId = assetType.getId();
        } else {
            log.warn("Asset type NOT DEFINED!");
        }
        productRequestFromImport.setAssetTypeId(assetTypeId);
        dynamicAssetService.create(productRequestFromImport);
    }

    protected void validateMasters(List<Master> masters, String scanId) {
        List<String> failedKeysList = masters.stream()
                .filter(x -> Objects.nonNull(x.getCodereadrScanId()) && !Objects.equals(scanId, x.getCodereadrScanId()))
                .map(Master::getPublicKey)
                .collect(Collectors.toList());
        if (!failedKeysList.isEmpty()) {
            String failedKeys = String.join("\n", failedKeysList);
            log.trace("failedKeys: {}", failedKeys);
            throw new DuplicateObjectException("Rejected keys already in use: \n" + failedKeys);
        }
    }

    protected List<Master> createOrFetchMasters(AssetImportDTO assetImportDTO) {
        List<String> publicKeys = getAssetsFromBarcode(assetImportDTO.getBarcodeItems());
        log.debug("ScanId: {}, Public_keys detected: {}", assetImportDTO.getScanId(), publicKeys);

        String cryptoCurrency = assetImportDTO.getQuestion("cryptoCurrency");
        if (Strings.isNullOrEmpty(cryptoCurrency)) {
            return masterService.findAllByPublicKeyIn(publicKeys);
        } else {
            return masterGeneratingService.generateMasters(publicKeys, cryptoCurrency, assetImportDTO.getScanTime());
        }
    }

    protected List<Master> importMasters(List<Master> masters, ImportWrappingDTO importWrappingDTO) throws PermissionDeniedException, DynamicAssetException {
        AssetImportDTO assetImportDTO = importWrappingDTO.getDynamicAssetImport();
        masterPermissionCheckingService.validateAssetImportPermission(masters);
        for (Master master : masters) {
            String assetId = master.getId();
            log.debug("Processing master: {}", assetId);
            master.setCodereadrScanId(assetImportDTO.getScanId());
            master.setImportDate(assetImportDTO.getScanTime());
            AssetType assetType = importWrappingDTO.getAssetType();
            if (Objects.nonNull(assetType)) {
                master.setAssetType(assetType);
                master.setIsCsc(assetType.getIsCsc());
            }
            if (Objects.nonNull(importWrappingDTO.getVendorAssetId())) {
                master.setOwnerAssetId(importWrappingDTO.getVendorAssetId());
            }
            master.setFile(Optional.ofNullable(importWrappingDTO.getProductMaster())
                    .map(Master::getFile).orElse(null));

            AssetRequest assetRequest = getProductRequestFromImport(assetId, importWrappingDTO);
            createDynamicAsset(master, assetRequest);
            log.debug("Successfully processed master: {}", assetId);
            onPostItemImported(master, importWrappingDTO);
        }
        return masters;
    }

    protected void onPostItemImported(Master master, ImportWrappingDTO assetImportDTO) {
        log.debug("onPostItemImport");
    }

    private void importBasicData(AssetImportDTO assetImportDTO, AssetImport assetImport) {
        assetImport = dynamicAssetImportMapper.mapAssetImport(assetImport, assetImportDTO);
        assetImport.setUpc(assetImportContextLoader.fetchUpc(assetImportDTO));
        assetImport.setMatched(true);
        assetImport.setError(null);
    }

    protected Map<String, FieldValueDTO> getDynamicQuestionFieldValues(AssetImportDTO dynamicAssetImport) {
        return codeReadrQuestionsMapper.getDynamicQuestionFieldValues(dynamicAssetImport, getDynamicFieldNamesMap(), getDynamicFieldConvertors());
    }

    protected Map<String, FieldValueDTO> getStaticFieldValues(ImportWrappingDTO assetImportDTO) {
        return codeReadrQuestionsMapper.getStaticFieldValues(assetImportDTO);
    }

    protected Map<String, String> getDynamicFieldNamesMap() {
        return Collections.emptyMap();
    }

    protected Map<String, Function<String, String>> getDynamicFieldConvertors() {
        return Collections.emptyMap();
    }

    private AssetImport createOrGetAssetImportByScanId(String scanId) {
        return assetImportRepository.findByScanId(scanId)
                .orElseGet(AssetImport::new);
    }

    private boolean isReimportAllowed(ImportWrappingDTO importWrappingDTO) {
        CodeReadrService codeReadrService = importWrappingDTO.getCodeReadrService();
        return Objects.nonNull(codeReadrService.getReimportAllowed()) && codeReadrService.getReimportAllowed();
    }

}
