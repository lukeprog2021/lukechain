import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class Lukechain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;

    public static boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0','0');

        for(int i=1;i<blockchain.size();i++){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //comparar os registros
            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("Hashes atuais não são iguais");
                return false;

            }
            if(!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("Hashes anteriores não são iguais");
                return false;
            }
            if(!currentBlock.hash.substring(0,difficulty).equals(hashTarget)){
                System.out.println("Este bloco não foi minerado");
                return false;
            }
        }
        return true;
    }



    public  static void main(String[] args){
        //definindo o bouncy castle como provedor de segurança
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Criar as carteiras
        walletA = new Wallet();
        walletB = new Wallet();

        //Teste das chaves privadas e chaves publicas
        System.out.println("Chave privada e Chave pública: ");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

        //Criamos a transação entre a e b
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 2,null);
        transaction.generateSignature(walletA.privateKey);

        //verificar a assinatura e a chave publica
        System.out.println("Assinatura sendo verificada");
        System.out.println(transaction.verifySignature());
/*
        blockchain.add(new Block("Olá eu sou o primeiro bloco","0"));
        System.out.println("Tentando minerar o bloco 1...");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Block("Oi, eu sou o segundo bloco",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Tentando minerar o bloco 2...");
        blockchain.get(1).mineBlock(difficulty);


        blockchain.add(new Block("Eai, eu sou o terceiro bloco",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Tentando minerar o bloco 3...");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\n Blockchain is valid: "+ isChainValid());


        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("The block chain: ");
        System.out.println(blockchainJson);

 */

    }
}
