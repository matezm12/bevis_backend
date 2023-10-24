package com.bevis.blockchaincore.domain;

import lombok.Data;

import javax.persistence.*;

@Table(name = "blockchain")
@Data
@Entity
public class Blockchain {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "node_address")
    private String nodeAddress;

    /**
     * Link to blockchain explorer to show transaction info for coin.
     */
    @Column(name = "explorer_url")
    private String explorerUrl;

    /**
     * Link to blockchain explorer to show address info for coin.
     */
    @Column(name = "address_url")
    private String addressUrl;

    /**
     * This field is used for Master generator,
     * to correctly generate asset ID from public key.
     * Should be the same regex as in keygen software.
     */
    @Column(name = "asset_id_regex")
    private String assetIdRegex;

    /**
     * This flag is used to enable currency price updating service,
     * which is scheduled job to download actual info from exchange rate services.
     */
    @Column(name = "exchange_rates")
    private boolean exchangeRates;

    /**
     * This flag used for currencies, which hide their balances from public.
     * Currently, used ONLY for Monero (XMR)
     */
    @Column(name = "private_balance")
    private boolean privateBalance;

    /**
     * This flag used to define tokens support for blockchain.
     */
    @Column(name = "has_tokens", nullable = false)
    private Boolean hasTokens = false;

    /**
     * If "balanceUpdate" is "true", then CoinBalance entity is
     * updating with Scheduled job service for this currency.
     */
    @Column(name = "balance_update", nullable = false)
    private Boolean balanceUpdate = false;

    /**
     * if "balanceEnable" is "true", then the server
     * is able to load balance for this currency.
     * Use this flag increase performance and prevent server
     * from loading balance for not supported currencies.
     */
    @Column(name = "balance_enable", nullable = false)
    private boolean balanceEnable = false;

    @Column(name = "balance_source")
    private String balanceSource;

    @Column(name = "job_balance_source")
    private String jobBalanceSource;

    @Column(name = "ipfs_icon")
    private String ipfsIcon;
}
