package com.lin.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.system.entity.Menu;
import org.apache.ibatis.annotations.Delete;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理 mapper
 */
public interface MenuMapper extends BaseMapper<Menu> {

    Menu get(Long menuId);

    List<Menu> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(Menu menu);

    int update(Menu menu);

    int remove(Long menuId);

    int batchRemove(Long[] menuIds);

    List<Menu> listMenuByUserId(Long id);

    List<String> listUserPerms(Long id);

    @Delete(" DELETE FROM SYS_MENU WHERE PARENT_ID = #{parentId} ")
    void deleteByParentId(Long parentId);
}
