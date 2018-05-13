package cn.edu.nju.software.bizservice;

import cn.edu.nju.software.common.pojo.bizservice.BizResponse;

/**
 * 业务层的用户权限验证
 */
public interface IdentityAuthority {
    /**
     * 验证用户的授权码是否正确
     * @param authorityCode
     * @return
     */
    BizResponse verifyAuthorityCode(String authorityCode);

}
