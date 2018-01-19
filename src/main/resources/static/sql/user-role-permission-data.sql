INSERT INTO `role`(`role_name`) VALUES ('admin'),('manager'),('normal');
INSERT INTO operation(`code`,`name`) VALUES ('add','新增'),('delete','删除'),('edit','修改'),('query','查询');
INSERT INTO `user`(`username`,`password`) VALUES ('admin','123456'),('manager','123456'),('normal','123456');
INSERT INTO `user_role`(`user_id`,`role_id`) VALUES (1,1),(2,2),(3,3);

INSERT INTO `user`(`username`,`password`,`salt`) 
VALUES ('admin','e82fd21cad812a7c3c3109b3db09d000','7db42eeaeec25aae6e113f09566fcf3f')
;