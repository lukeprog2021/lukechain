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

    public boolean processTransaction(){
        if(verifySignature() == false){
            System.out.println("#Verificação da transação falhou");
            return false;
        }
        for(TransactionInput i : inputs){
            i.UTXO = Lukechain.UTXOs.get(i.transactionOutputId);
        }
        //checa se a transação é válida
        if(getInputsValue() < Lukechain.minimumTransaction){
            System.out.println("Entrada de transações pequena: "+getInputsValue());
            return false;
        }

        //gerando as transações de output
        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient,value,transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        //adicionando as transações a lista
        for(TransactionOutput o : outputs){
            Lukechain.UTXOs.put(o.id,o);
        }

        //removendo as transações inputs de utxo lista
        for(TransactionInput i: inputs){
            if(i.UTXO == null)continue;
            Lukechain.UTXOs.remove(i.UTXO.id);
        }
        return true;
    }

    //retorna a soma do inputs(UTXOs) valores
    public float getInputsValue(){
        float total = 0;
        for(TransactionInput i : inputs){
            if(i.UTXO == null)continue;
            total += i.UTXO.value;
        }
        return total;
    }
    //retorna a soma dos outputs
    public  float getOutputsValue(){
        float total = 0;
        for(TransactionOutput o: outputs){
            total += o.value;
        }
        return total;
    }

}
