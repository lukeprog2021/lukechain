public class TransactionInput {
    public String transactionOutputId; //referencia para id da transação
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId){
        this.transactionOutputId = transactionOutputId;
    }
}
