import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
    public String transactionId; //assinatura da transação
    public PublicKey sender; //endereço de quem enviou (chave publica)
    public PublicKey recipient; //endereço de quem recebeu (chave publica)
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs){
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    //Calcula a aassinatura da transação ID
    private String calculateHash(){
        sequence++; //aumenta para evitar pares
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient)+
                        Float.toString(value) + sequence
        );

    }

    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(recipient)+Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey,data);
    }

    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender)+StringUtil.getStringFromKey(recipient)+Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

}
