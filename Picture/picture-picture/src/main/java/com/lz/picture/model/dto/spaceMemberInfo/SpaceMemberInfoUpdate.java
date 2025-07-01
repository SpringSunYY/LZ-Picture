package com.lz.picture.model.dto.spaceMemberInfo;

import com.lz.picture.model.domain.SpaceMemberInfo;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 空间成员信息Vo对象 p_space_member_info
 *
 * @author YY
 * @date 2025-03-24
 */
@Data
public class SpaceMemberInfoUpdate implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成员编号
     */
    @NotEmpty(message = "成员编号不能为空")
    private String memberId;


    /**
     * 角色（0创建者 1管理员 2编辑者 3浏览者）
     */
    @NotEmpty(message = "角色不能为空")
    private String roleType;

    /**
     * 对象转封装类
     *
     * @param spaceMemberInfoEdit 编辑对象
     * @return SpaceMemberInfo
     */
    public static SpaceMemberInfo editToObj(SpaceMemberInfoUpdate spaceMemberInfoEdit) {
        if (spaceMemberInfoEdit == null) {
            return null;
        }
        SpaceMemberInfo spaceMemberInfo = new SpaceMemberInfo();
        BeanUtils.copyProperties(spaceMemberInfoEdit, spaceMemberInfo);
        return spaceMemberInfo;
    }
}
