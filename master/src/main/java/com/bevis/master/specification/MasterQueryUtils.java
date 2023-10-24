package com.bevis.master.specification;

import com.bevis.master.domain.Asset;
import com.bevis.master.domain.Master;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bevis.common.util.CaseFormatUtil.toUnderscore;

public final class MasterQueryUtils {
    private final static String DYNAMIC_SORT_PREFIX = "dynamic-";

    public static void sort(Root<Master> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder, Sort sort) {
        if (Objects.nonNull(sort) && sort.isSorted()) {

            Sort s1 = Sort.by(
                    sort.stream().filter(x -> !x.getProperty().startsWith(DYNAMIC_SORT_PREFIX)).collect(Collectors.toList())
            );
            List<Order> staticOrders = QueryUtils.toOrders(s1, root, criteriaBuilder);

            Sort s2 = Sort.by(
                    sort.stream().filter(x -> x.getProperty().startsWith(DYNAMIC_SORT_PREFIX)).collect(Collectors.toList())
            );

            List<Order> dynamicOrders = toDynamicOrders(s2, criteriaBuilder, root);

            List<Order> orders = Stream.of(staticOrders, dynamicOrders)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            if (!orders.isEmpty()) {
                criteriaQuery.orderBy(orders);
            }
        }
    }

    static List<Order> toDynamicOrders(Sort sort, CriteriaBuilder cb, Root<Master> root) {
        return sort.stream().map(order -> {
            Join<Master, Asset> assetData = root.join("asset", JoinType.LEFT);
            Path<Map<String, Object>> attributesPath = assetData.get("attributes");
            String fieldName = order.getProperty().substring(DYNAMIC_SORT_PREFIX.length());
            Expression<String> literal = cb.literal("$." + toUnderscore(fieldName));
            Expression<String> jsonExtracted = cb.function("JSON_EXTRACT", String.class, attributesPath, literal);
            return order.getDirection() == Sort.Direction.ASC ? cb.asc(jsonExtracted) : cb.desc(jsonExtracted);
        }).collect(Collectors.toList());
    }
}
