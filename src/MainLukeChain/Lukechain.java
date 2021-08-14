package MainLukeChain;

import Bloco.Block;
import Carteira.Wallet;
import Transações.Transaction;
import Transações.TransactionInput;
import Transações.TransactionOutput;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class Lukechain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();


    public static int difficulty = 3;
    public static float minimumTransaction = 0.1f;
    public static Wallet walletA;
    public static Wallet walletB;
    public static Transaction genesisTransaction;


    public  static void main(String[] args){
        //definindo o bouncy castle como provedor de segurança
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        //Criar as carteiras
        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinBase = new Wallet();

        //criação da primeira transação (genesis), envio de 100 GadoCoins para WalletA
        genesisTransaction = new Transaction(coinBase.publicKey, walletA.publicKey, 100f,null);
        genesisTransaction.generateSignature(coinBase.privateKey);
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value, genesisTransaction.transactionId));
        UTXOs.put(genesisTransaction.outputs.get(0).id,genesisTransaction.outputs.get(0));

        System.out.println("Criando e minerando o Genesis Bloco.Block");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //teste
        Block block1 = new Block(genesis.hash);
        System.out.println("Balanço da carteira A é: "+ walletA.getBalance());
        System.out.println("A carteira A está tentando enviar (40) para Carteira B...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey,55.50f));
        addBlock(block1);
        System.out.println("Saldo da carteira A é:"+walletA.getBalance());
        System.out.println("Saldo da carteira B é:"+walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("A carteira A está tentando enviar (1000)...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey,1000f));
        addBlock(block2);
        System.out.println("Saldo da carteira A é: "+walletA.getBalance());
        System.out.println("Saldo da carteira B é: "+walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("A carteira B está tentando enviar (20) para carteira A");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey,20));
        System.out.println("Saldo da carteira A é: "+walletA.getBalance());
        System.out.println("Saldo da carteira B é: "+walletB.getBalance());

        isChainValid();

/*
        Teste das chaves privadas e chaves publicas
        System.out.println("Chave privada e Chave pública: ");
        System.out.println(MétodosUtil.StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(MétodosUtil.StringUtil.getStringFromKey(walletA.publicKey));


        Criamos a transação entre a e b
        Transações.Transaction transaction = new Transações.Transaction(walletA.publicKey, walletB.publicKey, 2,null);
        transaction.generateSignature(walletA.privateKey);



        //verificar a assinatura e a chave publica
        System.out.println("Assinatura sendo verificada");
        System.out.println(transaction.verifySignature());



        blockchain.add(new Bloco.Block("Olá eu sou o primeiro bloco","0"));
        System.out.println("Tentando minerar o bloco 1...");
        blockchain.get(0).mineBlock(difficulty);

        blockchain.add(new Bloco.Block("Oi, eu sou o segundo bloco",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Tentando minerar o bloco 2...");
        blockchain.get(1).mineBlock(difficulty);


        blockchain.add(new Bloco.Block("Eai, eu sou o terceiro bloco",blockchain.get(blockchain.size()-1).hash));
        System.out.println("Tentando minerar o bloco 3...");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\n Blockchain is valid: "+ isChainValid());


        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("The block chain: ");
        System.out.println(blockchainJson);

 */

    }

    private static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //comparar os registros
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("#Hashes atuais não são iguais");
                return false;

            }
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("#Hashes anteriores não são iguais");
                return false;
            }
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("#Este bloco não foi minerado");
                return false;
            }

            //loop transações na blockchain
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if (!currentTransaction.verifySignature()) {
                    System.out.println("#Assinatura da Transação(" + t + ") é inválida");
                    return false;
                }
                if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#As entradas não são iguais às saídas na transação(" + t + ")");
                    return false;
                }
                for (TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);
                    if (tempOutput == null) {
                        System.out.println("#Referencia do ID da Transação de entrada (" + t + ") foi perdida");
                        return false;
                    }
                    if (input.UTXO.value != tempOutput.value) {
                        System.out.println("#Referencia do valor da Transação de entrada (" + t + ") é inválida");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);

                }

                for (TransactionOutput output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
                    System.out.println("#Transação (" + t + ") de saída o destinátario não é quem deveria ser");
                    return false;
                }
                if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
                    System.out.println("#Transações.Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }


            }

        }
        System.out.println("Blockchain é válido");
        return true;
    }
}
