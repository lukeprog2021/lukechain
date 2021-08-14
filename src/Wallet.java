import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

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
}
