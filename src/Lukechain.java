import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Lukechain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static int difficulty = 5;

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

    }
}
