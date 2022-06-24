package com.shangchain.straitchain;

import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

/**
 * 2022/4/27
 * 开发用户向普通用户转账drop
 *
 * @author shangchain Junisyoan
 */
public class DevUserTransferUserDrop {
    // 替换信息可直接运行
    public static void main(String[] args) {
        String url="http://nps.shang-chain.com:30023/strait-chain-client-test";
        String from = "0x6d61dbfb32599cd94fee1c8fab1e80e2ee8623d7";
        String toAddress = "0xc4244f49522c32e6181b759f35be5efa2f19d7f9";
        String privateKey="0x30254413e44298281b625fa3702adc23c602635bb566709c412b4efb9ddb7a33";
        String appId="Ist8KOqm";
        String appKey="8d065964ab77cfa1917bdafa6c27e5dd605590ed";
        StraitChainClient client = new StraitChainClient();
        client.setAppId(appId);
        client.setAppKey(appKey);
        client.setUrl(url);

        // 获取gas
        BigInteger gasPrice = client.scsGasPrice();
        BigInteger gasLimit = new BigInteger("300000");

        Credentials credentials = Credentials.create(privateKey);

        //获取nonce
        BigInteger nonce = client.scsGetTransactionCount(from);

        // 金额 LONG ,1就是1DROP
        Double drop = 0.01;
        BigInteger value = BigInteger.valueOf((long) (drop * Math.pow(10, 18)));
        //创建交易
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, value);
        //签名Transaction，这里要对交易做签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        //发送交易
        String txHash = client.scsSendRawTransaction(hexValue);
        //获得到transactionHash后就可以查询这笔交易的状态了
        System.out.println(txHash);

        Address address = new Address("0x52802d27a62a99b4c178bf3cc28e8f3c2ee51932");
        System.out.println(address.getValue());
    }

}
