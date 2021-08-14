import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long timeStamp;
    private int nonce;

    public Block(String previousHash){

        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }
    public String calculateHash(){
        String calculatedHash = StringUtil.applySha256(
                    previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce)
                            + merkleRoot
        );
        return calculatedHash;
    }

    public void mineBlock(int difficulty){
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty);
        while(!hash.substring(0,difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Bloco minerado!!! : "+ hash);
    }

    public boolean addTransaction(Transaction transaction){
        //processa transão e valida, se for o bloco genesis ignora
        if(transaction == null)return false;
        if((previousHash != "0")){
            if((!transaction.processTransaction())){
                System.out.println("Falha na transação. Operação descartada");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transação adicionada com sucesso ao bloco");
        return true;
    }






}
