# Bevis BalanceCore module

## Development

Main class for loading balances from API & sync with database. 
    
    public interface CryptoBalanceLoader {
        Balance getWalletBalance(String walletId, String currency);
        List<Balance> getWalletBalances(List<WalletRequest> walletsToLoad);
    }

