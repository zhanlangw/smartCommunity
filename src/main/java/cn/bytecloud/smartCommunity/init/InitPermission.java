package cn.bytecloud.smartCommunity.init;

import cn.bytecloud.smartCommunity.annotation.Menu;
import cn.bytecloud.smartCommunity.menu.service.MenuService;
import cn.bytecloud.smartCommunity.permission.entity.Permission;
import cn.bytecloud.smartCommunity.permission.service.PermissionService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InitPermission {

    @Value("${scan.packages}")
    private String scanPackages;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PermissionService service;

    @PostConstruct
    public void initPermission() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Menu.class));
        scanner.findCandidateComponents("cn.bytecloud.smartCommunity.fence").forEach(item -> {
            String className = item.getBeanClassName();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
                Menu menu = clazz.getAnnotation(Menu.class);
                cn.bytecloud.smartCommunity.menu.entity.Menu tMenu = initMenu(menu.value(), null);
                initPermission(clazz, tMenu);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

    private void initPermission(Class<?> clazz, cn.bytecloud.smartCommunity.menu.entity.Menu menu) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            List<String> collect = Arrays.stream(method.getDeclaredAnnotations()).map(Annotation::annotationType).map
                    (Class::getName).collect(Collectors.toList());
            if ((collect.contains(GetMapping.class.getName()) || collect.contains(PostMapping.class.getName())) && collect.contains(RequiresPermissions.class.getName())) {
                String name;

                if (collect.contains(GetMapping.class.getName())) {
                    name = method.getDeclaredAnnotation(GetMapping.class).name();
                } else {
                    name = method.getDeclaredAnnotation(PostMapping.class).name();
                }
                if (EmptyUtil.isEmpty(name)) {
                    continue;
                }
                String interfaceUrl = method.getDeclaredAnnotation(RequiresPermissions.class).value()[0];

                if (service.findFirstByInterfaceUrl(interfaceUrl) != null) {
                    continue;
                }

                Permission perm = new Permission();
                perm.setName(name);
                perm.setInterfaceUrl(interfaceUrl);
                perm.setId(UUIDUtil.getUUID());
                if ((collect.contains(Menu.class.getName()))) {
                    cn.bytecloud.smartCommunity.menu.entity.Menu childMenu = initMenu(method.getDeclaredAnnotation(Menu.class).value(), menu.getId());
                    perm.setMenuId(childMenu.getId());
                } else {
                    perm.setMenuId(menu.getId());
                }
                service.save(perm);
            }

        }
    }


    public cn.bytecloud.smartCommunity.menu.entity.Menu initMenu(String name, String pid) {
        cn.bytecloud.smartCommunity.menu.entity.Menu menu = menuService.findFirstByNameAndPid(name, pid);
        if (menu == null) {
            menu = new cn.bytecloud.smartCommunity.menu.entity.Menu();
            menu.setId(UUIDUtil.getUUID());
            menu.setName(name);
            menu.setPid(pid);
            menuService.save(menu);
        }
        return menu;
    }
}

