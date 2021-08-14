import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap <String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public Wallet(){
        generateKeypair();
    }

    public void generateKeypair(){
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            //inicializa o gerador de chave
            keyGen.initialize(ecSpec,random);
            KeyPair keyPair = keyGen.generateKeyPair();
            //defini as chaves publica e privada
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //retorna o balanço e historico de UTXOs da carteira no this.UTXOs
    public  float getBalance(){
        float total = 0;
        for(Map.Entry<String, TransactionOutput> item: Lukechain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)){
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    //Gerar retornos de uma nova transação dessa carteira
    public Transaction sendFunds(PublicKey _recipient, float value){
        if(getBalance() < value ){
            System.out.println("Não tem fundo para enviar essa transação. Transação descartada");
            return null;
        }
        //criando um array list de inputs
        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for(Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value) break;
        }
        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }
}
