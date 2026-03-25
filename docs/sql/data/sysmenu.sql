INSERT INTO sys_menu (id, parent_id, menu_type, name, path, component, redirect, permission, title, icon, no_cache, breadcrumb, affix, hidden, always_show, sort, status, create_time, update_time, is_delete) VALUES ('00000000000000000000000000000009', '00000000000000000000000000000000', 1, '商品管理', '/product', 'Layout', '/product/page', null, '商品管理', null, 0, 1, 0, 0, 0, 2, 1, '2026-03-10 16:25:19', '2026-03-10 16:25:22', 0);
INSERT INTO sys_menu (id, parent_id, menu_type, name, path, component, redirect, permission, title, icon, no_cache, breadcrumb, affix, hidden, always_show, sort, status, create_time, update_time, is_delete) VALUES ('00000000000000000000000000000010', '00000000000000000000000000000009', 2, '商品列表', 'page', 'views/product', null, null, '商品管理', null, 0, 1, 0, 0, 0, 1, 1, '2026-03-10 16:26:55', '2026-03-10 16:26:57', 0);
INSERT INTO sys_menu (id, parent_id, menu_type, name, path, component, redirect, permission, title, icon, no_cache, breadcrumb, affix, hidden, always_show, sort, status, create_time, update_time, is_delete) VALUES ('00000000000000000000000000000011', '00000000000000000000000000000009', 2, '商品新增', 'create', 'views/product/form', null, null, '商品新增', null, 0, 1, 0, 1, 0, 2, 1, '2026-03-10 16:27:00', '2026-03-10 16:27:00', 0);
INSERT INTO sys_menu (id, parent_id, menu_type, name, path, component, redirect, permission, title, icon, no_cache, breadcrumb, affix, hidden, always_show, sort, status, create_time, update_time, is_delete) VALUES ('00000000000000000000000000000012', '00000000000000000000000000000009', 2, '商品修改', 'edit/:id', 'views/product/form', null, null, '商品修改', null, 0, 1, 0, 1, 0, 3, 1, '2026-03-10 16:27:00', '2026-03-10 16:27:00', 0);
INSERT INTO sys_menu (id, parent_id, menu_type, name, path, component, redirect,
                      permission, title, icon, no_cache, breadcrumb, affix, hidden,
                      always_show, sort, status, create_time, update_time, is_delete)
VALUES ('00000000000000000000000000000013',
        '0',
        0,
        '系统管理',
        '/system',
        'Layout',
        NULL,
        NULL,
        '系统管理',
        'system',
        0, 1, 0, 0, 1,
        99, 1, NOW(), NOW(), 0);

INSERT INTO sys_menu (id, parent_id, menu_type, name, path, component, redirect,
                      permission, title, icon, no_cache, breadcrumb, affix, hidden,
                      always_show, sort, status, create_time, update_time, is_delete)
VALUES ('00000000000000000000000000000014',
        '00000000000000000000000000000013',
        2,
        '用户管理',
        'user',
        'views/system/user',
        NULL,
        NULL,
        '用户管理',
        'user',
        0, 1, 0, 0, 0,
        1, 1, NOW(), NOW(), 0);
INSERT INTO sys_menu (id, parent_id, menu_type, name, path, component, redirect,
                      permission, title, icon, no_cache, breadcrumb, affix, hidden,
                      always_show, sort, status, create_time, update_time, is_delete)
VALUES ('00000000000000000000000000000015',
        '00000000000000000000000000000013',
        2,
        '数据字典',
        'dict',
        'views/system/dict',
        NULL,
        NULL,
        '数据字典',
        'dict',
        0, 1, 0, 0, 0,
        2, 1, NOW(), NOW(), 0);