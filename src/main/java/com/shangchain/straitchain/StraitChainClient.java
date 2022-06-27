package com.shangchain.straitchain;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.shangchain.straitchain.constants.StraitChainConstant;
import com.shangchain.straitchain.dto.BlockInfoDto;
import com.shangchain.straitchain.dto.NftMintDto;
import com.shangchain.straitchain.dto.StraitChainResponse;
import com.shangchain.straitchain.dto.TransactionInfoDto;
import com.shangchain.straitchain.exception.StraitChainException;
import com.shangchain.straitchain.params.*;
import com.shangchain.straitchain.service.IDepositCertificateContract;
import com.shangchain.straitchain.service.INftContract;
import com.shangchain.straitchain.service.IScsChain;
import com.shangchain.straitchain.service.Ipfs;
import com.shangchain.straitchain.utils.StraitChainUtil;
import lombok.Data;
import okhttp3.*;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 2022/4/26
 * 客户端
 *
 * @author shangchain Junisyoan
 */
@Data
public class StraitChainClient implements IScsChain, INftContract, IDepositCertificateContract, Ipfs {
    private String appId;
    private String appKey;
    private String url;
    protected OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();

    public StraitChainClient(String appId, String appKey, String url) {
        this.appId = appId;
        this.appKey = appKey;
        this.url = url;
    }

    public void refresh(String appId, String appKey, String url){
        this.appId = appId;
        this.appKey = appKey;
        this.url = url;
    }

    public StraitChainClient() {
    }

    protected StraitChainResponse commonRequest(RequestBody requestBody, String url) throws IOException {
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()){
            StraitChainResponse result = JSONObject.parseObject(response.body().string(), StraitChainResponse.class);
            if (result.getError()!=null){
                throw new StraitChainException(result.getError().getMessage(),result.getError().getCode());
            }
            return result;
        }
        throw new StraitChainException("请求失败："+response.message());
    }

    protected StraitChainResponse chainRequest(StraitChainParam straitChainParam) throws StraitChainException{
        if (StrUtil.isEmpty(url)) {
            throw new IllegalArgumentException("url不能为空");
        }
        String chainUri = "/api/develop/straits/action";

        RequestBody requestBody = RequestBody.create(JSONObject.toJSONString(straitChainParam), MediaType.parse("application/json"));
        try {
            return commonRequest(requestBody, url + chainUri);
        } catch (IOException e) {
            throw new StraitChainException(e);
        }
    }

    @Override
    public BigInteger scsProtocolVersion() throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_PROTOCOL_VERSION);
        StraitChainResponse response = chainRequest(param);
        return StraitChainUtil.toBigInteger(response.getResult().toString());
    }

    @Override
    public BigInteger scsGasPrice() throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GAS_PRICE);
        StraitChainResponse response = chainRequest(param);
        return StraitChainUtil.toBigInteger(response.getResult().toString());
    }

    @Override
    public BigInteger scsGetBalance(String address) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(address);
        list.add(DefaultBlockParameterName.LATEST.getValue());
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GET_BALANCE);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return StraitChainUtil.toBigInteger(response.getResult().toString());
    }

    @Override
    public BigInteger scsGetTransactionCount(String from) throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GET_TRANSACTION_COUNT);
        List<Object> list =new ArrayList<>();
        list.add(from);
        list.add(DefaultBlockParameterName.PENDING.getValue());
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return StraitChainUtil.toBigInteger(response.getResult().toString());
    }

    @Override
    public BigInteger scsGetBlockTransactionCountByHash(String blockHx) throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GET_BLOCK_TRANSACTION_COUNT_BY_HASH);
        List<Object> list =new ArrayList<>();
        list.add(blockHx);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return StraitChainUtil.toBigInteger(response.getResult().toString());
    }

    @Override
    public BigInteger scsGetBlockTransactionCountByNumber(Long blockNumber) throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GET_BLOCK_TRANSACTION_COUNT_BY_NUMBER);
        List<Object> list =new ArrayList<>();
        list.add(blockNumber);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return StraitChainUtil.toBigInteger(response.getResult().toString());
    }

    @Override
    public String scsGetCode(String address) throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GET_CODE);
        List<Object> list =new ArrayList<>();
        list.add(address);
        list.add(DefaultBlockParameterName.LATEST.getValue());
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    @Override
    public String scsSendTransaction(ScsSendTransactionParam scsParam) throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_SEND_TRANSACTION);
        List<Object> list =new ArrayList<>();
        list.add(scsParam.getFrom());
        list.add(scsParam.getTo());
        list.add(scsParam.getGas());
        // gasPrice
        BigInteger integer = scsGasPrice();
        list.add("0x"+integer.toString(16));
        list.add(scsParam.getValue());
        list.add(scsParam.getContractSignData());
        // nonce
        BigInteger nonce = scsGetTransactionCount(scsParam.getFrom());
        list.add("0x"+nonce.toString(16));
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    @Override
    public String scsSendRawTransaction(String txValue) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(txValue);
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_SEND_RAW_TRANSACTION);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    /**
     * 转移nft所属
     * @param params 参数
     * @return 交易哈希
     */
    public String transferNftUser(StraitChainSendRawTxParam params){
        BigInteger gasPrice = scsGasPrice();
        BigInteger nonce = scsGetTransactionCount(params.getFrom());
        BigInteger gasLimit = new BigInteger("110000");

        Function function = new Function(
                StraitChainConstant.CONTRACT_TRANSFER_FROM,
                Arrays.asList(new Address(160, params.getFrom())
                        ,new Address(160, params.getTo())
                        ,new Uint256(params.getTokenId())),
                Collections.emptyList());
        String encode = FunctionEncoder.encode(function);





        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, params.getContractAddress(), encode);
        Credentials credentials = Credentials.create(params.getPrivateKey());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String txValue = Numeric.toHexString(signedMessage);
        return scsSendRawTransaction(txValue);
    }


    @Override
    public BigInteger scsEstimateGas(ScsEstimateGasParam scsParam) throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_ESTIMATE_GAS);
        List<Object> list =new ArrayList<>();
        list.add(scsParam.getFrom());
        list.add(scsParam.getTo());
        list.add(scsParam.getGas());
        // gasPrice
        BigInteger integer = scsGasPrice();
        list.add("0x"+integer.toString(16));
        list.add(scsParam.getValue());
        list.add(scsParam.getContractSignData());
        // nonce
        BigInteger nonce = scsGetTransactionCount(scsParam.getFrom());
        list.add("0x"+nonce.toString(16));
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return StraitChainUtil.toBigInteger(response.getResult().toString());
    }

    @Override
    public String scsGetBlockByHashWithReturnHash(String address) throws StraitChainException {
        StraitChainResponse response = scsGetBlockByHash(address, false);
        return response.getResult().toString();
    }

    @Override
    public BlockInfoDto scsGetBlockByHashWithReturnObject(String address) throws StraitChainException {
        StraitChainResponse response = scsGetBlockByHash(address, true);
        return JSONObject.parseObject(response.getResult().toString(), BlockInfoDto.class);
    }

    private StraitChainResponse scsGetBlockByHash(String address,boolean returnObject){
        List<Object> list =new ArrayList<>();
        list.add(address);
        list.add(returnObject);
        StraitChainParam param = new StraitChainParam();
        param.setParams(list);
        param.setMethod(StraitChainConstant.SCS_GET_BLOCK_BY_HASH);
        return chainRequest(param);
    }

    @Override
    public String scsGetBlockByNumberWithReturnHash(String blockNumber) throws StraitChainException {
        StraitChainResponse response = scsGetBlockByNumber(blockNumber, false);
        return response.getResult().toString();
    }

    @Override
    public BlockInfoDto scsGetBlockByNumberWithReturnObject(String blockNumber) throws StraitChainException {
        StraitChainResponse response = scsGetBlockByNumber(blockNumber, true);
        return JSONObject.parseObject(response.getResult().toString(), BlockInfoDto.class);
    }

    private StraitChainResponse scsGetBlockByNumber(String blockNumber,boolean returnObject){
        List<Object> list =new ArrayList<>();
        list.add(blockNumber);
        list.add(returnObject);
        StraitChainParam param = new StraitChainParam();
        param.setParams(list);
        param.setMethod(StraitChainConstant.SCS_GET_BLOCK_BY_NUMBER);
        return chainRequest(param);
    }

    @Override
    public TransactionInfoDto scsGetTransactionReceipt(String txHash) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(txHash);
        StraitChainParam param = new StraitChainParam();
        param.setParams(list);
        param.setMethod(StraitChainConstant.SCS_GET_TRANSACTION_RECEIPT);
        StraitChainResponse response = chainRequest(param);
        return JSONObject.parseObject(response.getResult().toString(),TransactionInfoDto.class);

    }

    @Override
    public TransactionInfoDto scsGetTransactionByHash(String txHash) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(txHash);
        StraitChainParam param = new StraitChainParam();
        param.setParams(list);
        param.setMethod(StraitChainConstant.SCS_GET_TRANSACTION_BY_HASH);
        StraitChainResponse response = chainRequest(param);
        return JSONObject.parseObject(response.getResult().toString(),TransactionInfoDto.class);
    }

    @Override
    public String scsBlockNumber() throws StraitChainException {
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_BLOCK_NUMBER);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    @Override
    public String scsNftMint(StraitNftMintParam nftMintParam) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(appId);
        list.add(nftMintParam.getNftName());
        list.add(nftMintParam.getCid());
        list.add(nftMintParam.getNftUri());
        list.add(nftMintParam.getCopyRight());
        list.add(nftMintParam.getIssuer());
        list.add(nftMintParam.getOperator());
        list.add(nftMintParam.getRemark());
        list.add(nftMintParam.getCount());
        list.add(nftMintParam.getOwner());
        list.add(nftMintParam.getContractAddress());
        list.add(nftMintParam.getCollectSn());
        list.add(nftMintParam.getServiceId());
        String md5 = StraitChainUtil.encryptDataByMd5(list, appKey);
        list.add(md5);
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_NFT_MINT);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    @Override
    public String scsDeployContract(String from,Integer count) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(from);
        list.add(count);
        list.add(appId);
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_DEPLOY_CONTRACT);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    @Override
    public String scsContractAddressByHash(String txHash) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(txHash);
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_CONTRACT_ADDRESS_BY_HASH);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    @Override
    public String scsCall(ScsCallParam scsParam, String blockNumber) throws StraitChainException {
        BigInteger gasPrice = scsGetTransactionCount(scsParam.getFrom());
        scsParam.setGasPrice("0x"+gasPrice.toString(16));
        List<Object> list =new ArrayList<>();
        list.add(scsParam);
        list.add(blockNumber);
        StraitChainParam param = new StraitChainParam();
        param.setParams(list);
        param.setMethod(StraitChainConstant.SCS_CALL);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }

    @Override
    public List<NftMintDto> scsGetTokenByHash(String txHash) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(txHash);
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GET_TOKEN_BY_HASH);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return JSONObject.parseArray(response.getResult().toString(), NftMintDto.class);
    }

    @Override
    public String scsGetEvidenceContractAddress() throws StraitChainException {
        List<Object> list =new ArrayList<>();
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_GET_EVIDENCE_CONTRACT_ADDRESS);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();

    }

    @Override
    public String scsExistingEvidence(StraitChainExistingEvidenceParam existingEvidenceParam) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(appId);
        list.add(existingEvidenceParam.getServiceId());
        list.add(existingEvidenceParam.getCid());
        list.add(existingEvidenceParam.getContent());
        list.add(existingEvidenceParam.getContractSignHex());
        String md5 = StraitChainUtil.encryptDataByMd5(list, appKey);
        list.add(md5);
        StraitChainParam param = new StraitChainParam();
        param.setMethod(StraitChainConstant.SCS_EXISTING_EVIDENCE);
        param.setParams(list);
        StraitChainResponse response = chainRequest(param);
        return response.getResult().toString();
    }


    @Override
    public void dcEvidence() throws StraitChainException {
        // 这里是方法签名，在com.shangchain.straitchain.StraitChainClient.dcEvidenceSignHex中已经写好
    }

    @Override
    public String dcEvidenceSignHex(StraitChainContractDcEvidenceSignHexParam param) throws StraitChainException {
        // 获取合约地址
        String contractAddress = scsGetEvidenceContractAddress();

        // 获取gasPrice
        BigInteger gasPrice = scsGasPrice();

        // 获取nonce
        BigInteger nonce = scsGetTransactionCount(param.getFrom());

        // 方法编码
        Function function = new Function(
                StraitChainConstant.CONTRACT_EVIDENCE,
                Arrays.asList(new Utf8String(param.getCid()),new Utf8String(param.getContent())),
                Collections.emptyList());
        String encode = FunctionEncoder.encode(function);

        // 合约签名
        BigInteger gasLimit = new BigInteger("110000");
        Credentials credentials = Credentials.create(param.getPrivateKey());
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, encode);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Numeric.toHexString(signedMessage);
    }

    @Override
    public String ipfsUpload(String address, File file) throws StraitChainException {
        List<Object> list =new ArrayList<>();
        list.add(appId);
        list.add(address);

        String signData = StraitChainUtil.encryptDataByMd5(list, appKey);
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file,MediaType.parse("text/plain")))
                .addFormDataPart("address", address)
                .addFormDataPart("appId", appId)
                .addFormDataPart("sign", signData)
                .build();
        try {
            StraitChainResponse result = commonRequest(formBody, url + "/api/develop/ipfsUpload");
            return result.getResult().toString();
        } catch (IOException e) {
            throw new StraitChainException(e);
        }
    }

    @Override
    public String nftOwnerOf(String from, String contractAddress, Integer tokenId) throws StraitChainException {
        Function function = new Function(
                StraitChainConstant.CONTRACT_OWNER_OF,
                Collections.singletonList(new Uint256(tokenId)),
                Collections.emptyList());
        String encode = FunctionEncoder.encode(function);

        ScsCallParam param = new ScsCallParam();
        param.setFrom(from);
        param.setTo(contractAddress);
        param.setGas("0x34455");
        param.setValue("");
        param.setData(encode);
        return scsCall(param, DefaultBlockParameterName.LATEST.getValue());

    }
}
