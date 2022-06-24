package com.shangchain.straitchain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 2022/4/27
 * 交易信息，参考文档
 *
 * @author shangchain Junisyoan
 */
@NoArgsConstructor
@Data
public class TransactionInfoDto {
    public String blockHash;
    public String logsBloom;
    public String contractAddress;
    public String transactionIndex;
    public String type;
    public String transactionHash;
    public String gasUsed;
    public String blockNumber;
    public String cumulativeGasUsed;
    public String from;
    public String to;
    public List<Logs> logs;
    public String status;

    @NoArgsConstructor
    @Data
    public static class Logs {
        public String blockHash;
        public String address;
        public String logIndex;
        public String data;
        public Boolean removed;
        public List<String> topics;
        public String blockNumber;
        public String transactionIndex;
        public String transactionHash;
    }
}
