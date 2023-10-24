# Bevis Back-End

## Services for loading coin balance

###Gateway links

AWS API Gateway for loading balances from different balance sources (services) “blockchain-balance-api”
https://console.aws.amazon.com/apigateway/home?region=us-east-1#/apis/jw6pgpgzqk/resources

Full Doc for Implementing AWS API Gateway
https://docs.google.com/document/d/1hEMVA4nPiYUKDYqsnT1bIkW0FD_V9HrvCzxA_vZEHU0/

###Route architecture:

Base URL:
https://balance.bevis.sg

Custom Timeout: 4000 ms

Request headers:
Security header:
X-API-KEY: {AWS_API_GATEWAY_API_KEY}

Accept: application/json
Content-Type: application/json

Loading Single Balance:

    GET https://balance.bevis.sg/v3/balance/{currency}/{source}/{address}

Response Body:

    {
        "currency": "{currency}",
        "address": "{address}",
        "balance": "{balance}",
        "divider": "{divider}"
    }


Loading Batch of Multiple balances (POST):

    POST https://balance.bevis.sg/v3/balance/{currency}/{source}/

Request Body

    {
        "addresses":[
            "{address1}",
            "{address2}"
        ]
    }

Loading Batch of Multiple balances (GET)):

    GET https://balance.bevis.sg/v3/balance/{currency}/{source}/
    ?addresses={address1},{address2}

Response Body:

    {
        "data": [
            {
                "currency": "{currency}",
                "address": "{address1}",
                "balance": "{balance1}",
                "divider": "{divider}"
            },
            {
                "currency": "{currency}",
                "address": "{address2}",
                "balance": "{balance2}",
                "divider": "{divider}"
            }
        ]
    }

Error responses:

4xx Response body:

    {
        "statusCode": {4xx},
        "message": "{message}"
    }


5xx Response body:

    {
        "statusCode": {5xx},
        "message": "{message}"
    }

## Loading Coin Balances

Non-Blocking WebClient, written on Kotlin for loading Balances from AWS API Gateway API 

    interface CryptoBalanceWebClient {
        suspend fun loadSingleBalance(currency: String, address: String, source: String): Balance
        suspend fun loadMultiBalancesDefault(currency: String, addresses: List<String>, source: String): ListDataResponse<Balance>
        suspend fun loadMultiBalancesPost(currency: String, addresses: List<String>, source: String): ListDataResponse<Balance>
        suspend fun loadMultiBalancesGet(currency: String, addresses: List<String>, source: String): ListDataResponse<Balance>
    }

Main Service for Asynchronous loading Coin Balances using CryptoBalanceWebClient. Using non-blocking Kotlin coroutines. 
Loading data in parallel requests. Trying to load balances in single request when possible.

    interface CryptoBalanceApiService {
        suspend fun loadSingleBalance(currency: String, address: String): Balance?
        suspend fun loadSingleBalance(currency: String, address: String, source: String): Balance?
        suspend fun loadBalances(wallets: List<WalletRequest>): List<Balance>
        suspend fun loadBalances(currency: String, addresses: List<String>, source: String): List<Balance>
        suspend fun loadBalances(currency: String, addresses: List<String>): List<Balance>
    }
