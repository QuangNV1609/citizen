-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 09, 2021 lúc 05:35 PM
-- Phiên bản máy phục vụ: 10.4.21-MariaDB
-- Phiên bản PHP: 7.3.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `citizen`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `city_entity`
--

CREATE TABLE `city_entity` (
  `city_id` int(11) NOT NULL,
  `city_name` varchar(30) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `declaration_time`
--

CREATE TABLE `declaration_time` (
  `declaration_time_id` varchar(255) NOT NULL,
  `end` datetime DEFAULT NULL,
  `start` datetime DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `district_entity`
--

CREATE TABLE `district_entity` (
  `district_id` int(11) NOT NULL,
  `district_name` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `people_entity`
--

CREATE TABLE `people_entity` (
  `people_id` varchar(255) NOT NULL,
  `citizen_id` varchar(255) DEFAULT NULL,
  `create_at` datetime DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `educational_level` int(11) DEFAULT NULL,
  `gender` bit(1) DEFAULT NULL,
  `job` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `name` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `religion` varchar(15) CHARACTER SET utf8 DEFAULT NULL,
  `home_town` int(11) DEFAULT NULL,
  `permanent_address` int(11) DEFAULT NULL,
  `staying_address` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user_entity`
--

CREATE TABLE `user_entity` (
  `username` varchar(255) NOT NULL,
  `create_at` datetime DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `enable` bit(1) DEFAULT NULL,
  `last_modified_at` datetime DEFAULT NULL,
  `last_modified_by` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `user_role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `village_entity`
--

CREATE TABLE `village_entity` (
  `village_id` int(11) NOT NULL,
  `village_name` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `ward_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ward_entity`
--

CREATE TABLE `ward_entity` (
  `ward_id` int(11) NOT NULL,
  `ward_name` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `district_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `city_entity`
--
ALTER TABLE `city_entity`
  ADD PRIMARY KEY (`city_id`);

--
-- Chỉ mục cho bảng `declaration_time`
--
ALTER TABLE `declaration_time`
  ADD PRIMARY KEY (`declaration_time_id`),
  ADD KEY `FKsonmfqqprh3rbw6o6vmhulpix` (`user_id`);

--
-- Chỉ mục cho bảng `district_entity`
--
ALTER TABLE `district_entity`
  ADD PRIMARY KEY (`district_id`),
  ADD KEY `FKtk4k16gbl3sl94h4t38qy901n` (`city_id`);

--
-- Chỉ mục cho bảng `people_entity`
--
ALTER TABLE `people_entity`
  ADD PRIMARY KEY (`people_id`),
  ADD KEY `FKc2ydhewne3hybhc0n238umvgs` (`home_town`),
  ADD KEY `FKqh72w5p5rfm63o3ugps4vpxfl` (`permanent_address`),
  ADD KEY `FKhpwaoer0oq4tknead04w5rcqw` (`staying_address`);

--
-- Chỉ mục cho bảng `user_entity`
--
ALTER TABLE `user_entity`
  ADD PRIMARY KEY (`username`);

--
-- Chỉ mục cho bảng `village_entity`
--
ALTER TABLE `village_entity`
  ADD PRIMARY KEY (`village_id`),
  ADD KEY `FKne4vner1exq4ync0dv1gxuwcl` (`ward_id`);

--
-- Chỉ mục cho bảng `ward_entity`
--
ALTER TABLE `ward_entity`
  ADD PRIMARY KEY (`ward_id`),
  ADD KEY `FKgpwytdq32rq3bi4swm2owttd4` (`district_id`);

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `declaration_time`
--
ALTER TABLE `declaration_time`
  ADD CONSTRAINT `FKsonmfqqprh3rbw6o6vmhulpix` FOREIGN KEY (`user_id`) REFERENCES `user_entity` (`username`);

--
-- Các ràng buộc cho bảng `district_entity`
--
ALTER TABLE `district_entity`
  ADD CONSTRAINT `FKtk4k16gbl3sl94h4t38qy901n` FOREIGN KEY (`city_id`) REFERENCES `city_entity` (`city_id`);

--
-- Các ràng buộc cho bảng `people_entity`
--
ALTER TABLE `people_entity`
  ADD CONSTRAINT `FKc2ydhewne3hybhc0n238umvgs` FOREIGN KEY (`home_town`) REFERENCES `village_entity` (`village_id`),
  ADD CONSTRAINT `FKhpwaoer0oq4tknead04w5rcqw` FOREIGN KEY (`staying_address`) REFERENCES `village_entity` (`village_id`),
  ADD CONSTRAINT `FKqh72w5p5rfm63o3ugps4vpxfl` FOREIGN KEY (`permanent_address`) REFERENCES `village_entity` (`village_id`);

--
-- Các ràng buộc cho bảng `village_entity`
--
ALTER TABLE `village_entity`
  ADD CONSTRAINT `FKne4vner1exq4ync0dv1gxuwcl` FOREIGN KEY (`ward_id`) REFERENCES `ward_entity` (`ward_id`);

--
-- Các ràng buộc cho bảng `ward_entity`
--
ALTER TABLE `ward_entity`
  ADD CONSTRAINT `FKgpwytdq32rq3bi4swm2owttd4` FOREIGN KEY (`district_id`) REFERENCES `district_entity` (`district_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
