package com.shangchain.straitchain.service;

import com.shangchain.straitchain.dto.BlockInfoDto;
import com.shangchain.straitchain.dto.NftMintDto;
import com.shangchain.straitchain.dto.TransactionInfoDto;
import com.shangchain.straitchain.exception.StraitChainException;
import com.shangchain.straitchain.params.*;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;
import java.util.List;

/**
 * 2022/4/26
 * 链接口
 *
 * @author shangchain Junisyoan
 */
public interface IScsChain {
    /**
     * 链版本号
     * @return 链版本号
     */
    BigInteger scsProtocolVersion() throws StraitChainException;

    /**
     * 系统建议费用
     * @return 系统建议费用
     */
    BigInteger scsGasPrice() throws StraitChainException;

    /**
     * 账户余额，需要除以 18个0 Math.pow(10,18)
     * @param address 通行证地址
     * @return 账户余额
     */
    BigInteger scsGetBalance(String address) throws StraitChainException;

    /**
     * 获取nonce
     * @param from 通行证地址
     * @return 获取nonce
     */
    BigInteger scsGetTransactionCount(String from) throws StraitChainException;

    /**
     * 块内的交易数量
     * @param blockHx 块哈希
     * @return 块内的交易数量
     */
    BigInteger scsGetBlockTransactionCountByHash(String blockHx) throws StraitChainException;

    /**
     * 块内的交易数量
     * @param blockNumber 块号
     * @return 块内的交易数量
     */
    BigInteger scsGetBlockTransactionCountByNumber(Long blockNumber) throws StraitChainException;

    /**
     * 该地址的代码
     * @param address 地址
     * @return 该地址的代码
     */
    String scsGetCode(String address) throws StraitChainException;

    /**
     * 发送交易
     * @param scsParam 参数
     * @return 交易哈希
     */
    String scsSendTransaction(ScsSendTransactionParam scsParam) throws StraitChainException;

    /**
     * 发送交易
     * @param txValue 哈希
     */
    String scsSendRawTransaction(String txValue) throws StraitChainException;

    /**
     * 估算消耗的gas费用
     * @param scsParam 参数
     * @return 估算消耗的gas费用
     */
    BigInteger scsEstimateGas(ScsEstimateGasParam scsParam) throws StraitChainException;

    /**
     * 根据块哈希返回块的交易哈希
     * @param address 块哈希
     * @return 交易哈希
     */
    String scsGetBlockByHashWithReturnHash(String address) throws StraitChainException;

    /**
     * 根据块哈希返回块对象信息
     * @param address 块哈希
     * @return 块信息
     */
    BlockInfoDto scsGetBlockByHashWithReturnObject(String address) throws StraitChainException;

    /**
     * 根据块号返回块的交易哈希
     * @param blockNumber 块号
     * @return 交易哈希
     */
    String scsGetBlockByNumberWithReturnHash(String blockNumber) throws StraitChainException;
    /**
     * 根据块号返回块对象信息
     * @param blockNumber 块号
     * @return 块信息
     */
    BlockInfoDto scsGetBlockByNumberWithReturnObject(String blockNumber) throws StraitChainException;

    /**
     * 获取交易详情
     * @param txHash 交易hash
     * @return 交易信息
     */
    TransactionInfoDto scsGetTransactionReceipt(String txHash) throws StraitChainException;

    /**
     * 获取交易详情
     * @param txHash 交易hash
     * @return 交易信息
     */
    TransactionInfoDto scsGetTransactionByHash(String txHash) throws StraitChainException;

    /**
     * 最新的块编号
     * @return 最新的块编号
     */
    String scsBlockNumber() throws StraitChainException;

    /**
     * 铸造nft
     * @param nftMintParam 参数
     * @return 交易哈希
     */
    String scsNftMint(StraitNftMintParam nftMintParam) throws StraitChainException;

    /**
     * 部署合约
     * @param count 准备铸造的nft个数
     * @param from 通行证地址
     * @return 交易哈希
     */
    String scsDeployContract(String from,Integer count) throws StraitChainException;

    /**
     * 根据scsDeployContract返回的交易哈希，获取合约地址
     * @param txHash 合约部署
     * @return 合约地址
     */
    String scsContractAddressByHash(String txHash) throws StraitChainException;

    /**
     * 调用链上交易
     * @param scsParam 参数
     * @param blockNumber 块号
     * @return 交易哈希
     * @see DefaultBlockParameterName
     */
    String scsCall(ScsCallParam scsParam,String blockNumber) throws StraitChainException;

    /**
     * 根据scsNftMint返回的交易哈希，获取nft数据
     * @param txHash 交易hash
     * @return NftMintDto
     */
    List<NftMintDto> scsGetTokenByHash(String txHash) throws StraitChainException;

    /**
     * 获取系统通用存证合约地址
     * @return 哈希地址
     */
    String scsGetEvidenceContractAddress() throws StraitChainException;

    /**
     * 通用存证功能
     * @param existingEvidenceParam 参数
     * @return 交易哈希
     */
    String scsExistingEvidence(StraitChainExistingEvidenceParam existingEvidenceParam) throws StraitChainException;
}
