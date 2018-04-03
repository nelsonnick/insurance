CREATE TABLE `location` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`name` varchar(255) NULL COMMENT '名称',

PRIMARY KEY (`id`)

);



CREATE TABLE `user` (

`id` int NOT NULL AUTO_INCREMENT COMMENT '序号',

`lid` int NULL COMMENT '地点序号',

`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '人员姓名',

`login` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录名称',

`pass` varchar(255) CHARACTER SET utf8 NULL COMMENT '登录密码',

`state` int NULL COMMENT '0停用1启用',

PRIMARY KEY (`id`)

);



CREATE TABLE `person` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`tid` int(11) NULL COMMENT '人员类别',

`lid` int(11) NULL COMMENT '中心序号',

`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '身份证号码',

`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',

`sex` int(11) NULL COMMENT '1男2女',

`birth` date NULL COMMENT '出生日期',

`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系电话',

`address` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系地址',

`marriage` int(11) NULL COMMENT '1未婚2已婚3离异4丧偶',

`state` int(11) NULL COMMENT '0未享受1正在享受',

`delay` int(11) NULL COMMENT '0不延期1延期',

`remark` varchar(999) CHARACTER SET utf8 NULL COMMENT '备注',

PRIMARY KEY (`id`)

);



CREATE TABLE `type` (

`id` int NOT NULL AUTO_INCREMENT,

`category` int(11) NULL COMMENT '1灵活就业2公益岗位3企业吸纳',

`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '人员类别',

PRIMARY KEY (`id`)

);



CREATE TABLE `family` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`pid` int(11) NULL COMMENT '人员',

`number` varchar(255) CHARACTER SET utf8 NULL COMMENT '身份证号码',

`name` varchar(255) CHARACTER SET utf8 NULL COMMENT '姓名',

`sex` int(11) NULL COMMENT '1男2女',

`phone` varchar(255) CHARACTER SET utf8 NULL COMMENT '联系电话',

`identity` int(11) NULL COMMENT '1夫2妻3子4女5父6母7兄弟8姐妹',

`marriage` int(11) NULL COMMENT '1未婚2已婚3离异4丧偶',

`birth` date NULL COMMENT '出生日期',

`state` int(11) NULL COMMENT '0未享受1正在享受',

`remark` varchar(999) CHARACTER SET utf8 NULL COMMENT '备注',

PRIMARY KEY (`id`)

);



CREATE TABLE `changePerson` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',

`pid` int(11) NULL COMMENT '人员',

`uid` int(11) NULL COMMENT '用户',

`type` int(11) NULL COMMENT '1新增2信息变更3状态变更',

`time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '变更时间',

`before` varchar(999) CHARACTER SET utf8 NULL COMMENT '之前的信息',

`after` varchar(999) CHARACTER SET utf8 NULL COMMENT '之后的信息',

PRIMARY KEY (`id`)

);



CREATE TABLE `changeFamily` (

`id` int(11) NOT NULL AUTO_INCREMENT COMMENT ' 序号',

`fid` int(11) NULL COMMENT '家庭成员',

`uid` int(11) NULL COMMENT '用户',

`type` int(11) NULL COMMENT '1新增2信息变更3状态变更',

`time` datetime NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '变更时间',

`before` varchar(999) CHARACTER SET utf8 NULL COMMENT '之前的信息',

`after` varchar(999) CHARACTER SET utf8 NULL COMMENT '之后的信息',

PRIMARY KEY (`id`)

);





ALTER TABLE `user` ADD CONSTRAINT `location_user` FOREIGN KEY (`lid`) REFERENCES `location` (`id`);

ALTER TABLE `person` ADD CONSTRAINT `person_type` FOREIGN KEY (`tid`) REFERENCES `type` (`id`);

ALTER TABLE `person` ADD CONSTRAINT `person_location` FOREIGN KEY (`lid`) REFERENCES `location` (`id`);

ALTER TABLE `family` ADD CONSTRAINT `family_person` FOREIGN KEY (`pid`) REFERENCES `person` (`id`);

ALTER TABLE `changePerson` ADD CONSTRAINT `changePerson_person` FOREIGN KEY (`pid`) REFERENCES `person` (`id`);

ALTER TABLE `changePerson` ADD CONSTRAINT `changePerson_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`);

ALTER TABLE `changeFamily` ADD CONSTRAINT `changeFamily_family` FOREIGN KEY (`fid`) REFERENCES `family` (`id`);

ALTER TABLE `changeFamily` ADD CONSTRAINT `changeFamily_user` FOREIGN KEY (`uid`) REFERENCES `user` (`id`);



