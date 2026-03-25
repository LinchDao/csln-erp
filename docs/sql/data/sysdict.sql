-- =============================================
-- 系统数据字典：尺码（顶级字典）
-- =============================================
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('DICT_SIZE', '0', '尺码', 'SIZE', 1, 1, 0, '服装尺码');

-- 尺码子项
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('SIZE_S', 'DICT_SIZE', 'S', 'S', 1, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('SIZE_M', 'DICT_SIZE', 'M', 'M', 2, 1, 1, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('SIZE_L', 'DICT_SIZE', 'L', 'L', 3, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('SIZE_XL', 'DICT_SIZE', 'XL', 'XL', 4, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('SIZE_XXL', 'DICT_SIZE', 'XXL', 'XXL', 5, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('SIZE_XXXL', 'DICT_SIZE', 'XXXL', 'XXXL', 6, 1, 0, '');

-- =============================================
-- 系统数据字典：颜色（顶级字典）
-- =============================================
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('DICT_COLOR', '0', '颜色', 'COLOR', 2, 1, 0, '服装颜色');

-- 颜色子项（10种）
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_WHITE', 'DICT_COLOR', '白色', '白色', 1, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_BLACK', 'DICT_COLOR', '黑色', '黑色', 2, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_RED', 'DICT_COLOR', '红色', '红色', 3, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_BLUE', 'DICT_COLOR', '蓝色', '蓝色', 4, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_YELLOW', 'DICT_COLOR', '黄色', '黄色', 5, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_GREEN', 'DICT_COLOR', '绿色', '绿色', 6, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_GRAY', 'DICT_COLOR', '灰色', '灰色', 7, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_PINK', 'DICT_COLOR', '粉色', '粉色', 8, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_PURPLE', 'DICT_COLOR', '紫色', '紫色', 9, 1, 0, '');
INSERT INTO sys_dict (id, parent_id, dict_name, dict_value, sort, status, is_delete, remark)
VALUES ('COLOR_BROWN', 'DICT_COLOR', '棕色', '棕色', 10, 1, 0, '');