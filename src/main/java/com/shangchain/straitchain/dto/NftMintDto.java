package com.shangchain.straitchain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 2022/4/26
 * nft铸造结果
 *
 * @author shangchain Junisyoan
 */
@Data
public class NftMintDto implements Serializable {
    String hash;
    Integer tokenId;
}
