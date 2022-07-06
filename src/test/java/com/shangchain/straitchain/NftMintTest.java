package com.shangchain.straitchain;

import com.shangchain.straitchain.params.StraitChainSendRawTxParam;

/**
 * 2022/4/26
 * nft铸造
 *
 * @author shangchain Junisyoan
 */
public class NftMintTest {

    public static void main(String[] args) {
        StraitChainClient client = new StraitChainClient();
        client.setAppId("47yMIGZH");
        client.setAppKey("974a49c993770eedb2e2857f285e8de241649ac4");
        client.setUrl("https://backend.straitchain.com/webclient");
        String from       = "0x92855a8153b13d57cc4f2c4174e68bcefaea79d4";
        String to         = "0x92855a8153b13d57cc4f2c4174e68bcefaea79d4";
        String privateKey = "0x663543de05a44698b43abfe66f10dbd65653d5deea1cead3c9644fd265979396";

//        int nftMintCount = 10;
//        // 部署合约
//        String contractTxHash = client.scsDeployContract(from,nftMintCount);
//
//        // 根据合约哈希查询合约地址
//        String contractAddress = client.scsContractAddressByHash(contractTxHash);

//        // 开始铸造
//        StraitNftMintParam nftMintParam = new StraitNftMintParam();
//        nftMintParam.setNftName("这是nft名字");
//        nftMintParam.setCid("这是ipfs上传后的cid，没有可不要");
//        nftMintParam.setNftUri("http://nps.shang-chain.com:30023/profile/tmp/ahjdfhf.json");
//        nftMintParam.setCopyRight("版权方");
//        nftMintParam.setIssuer("发行方");
//        nftMintParam.setOperator("运营方");
//        nftMintParam.setRemark("这是一个备注");
//        nftMintParam.setCount(nftMintCount);
//        nftMintParam.setOwner(from);
//        nftMintParam.setContractAddress(contractAddress);
//        // 业务需求，全平台唯一。如：公司缩写+uuid
//        nftMintParam.setCollectSn("strait_chain_test_1");
//        // 对应的服务ID，没有就空
//        nftMintParam.setServiceId("");
//        String nftMintHash = client.scsNftMint(nftMintParam);
//
//        // 查询铸造状态
//        List<NftMintDto> list = client.scsGetTokenByHash(nftMintHash);
//        for (NftMintDto dto : list) {
//            // 铸造没完成为null
//            System.out.println(dto.getHash());
//            // 铸造没完成为null
//            System.out.println(dto.getTokenId());
//        }

        // 转移nft
        StraitChainSendRawTxParam param = new StraitChainSendRawTxParam();
        param.setFrom(from);
        param.setTo(to);
        param.setContractAddress("0x8a1bb40d3e395470bc7f478f713cd771a31410c1");
        // 上面的tokenId
        param.setTokenId(1);
        param.setPrivateKey(privateKey);
        String transferHx = client.transferNftUser(param);
        System.out.println(transferHx);




    }


}
