-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 25, 2024 at 10:41 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bank_sampah`
--

-- --------------------------------------------------------

--
-- Table structure for table `failed_jobs`
--

CREATE TABLE `failed_jobs` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `connection` text NOT NULL,
  `queue` text NOT NULL,
  `payload` longtext NOT NULL,
  `exception` longtext NOT NULL,
  `failed_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(255) NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '2014_10_12_000000_create_users_table', 1),
(2, '2014_10_12_100000_create_password_reset_tokens_table', 1),
(3, '2019_08_19_000000_create_failed_jobs_table', 1),
(4, '2019_12_14_000001_create_personal_access_tokens_table', 1);

-- --------------------------------------------------------

--
-- Table structure for table `mtcategory`
--

CREATE TABLE `mtcategory` (
  `idcategory` int(11) NOT NULL,
  `namecategory` varchar(255) DEFAULT NULL,
  `inuserid` varchar(255) DEFAULT NULL,
  `chuserid` varchar(255) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  `chdate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mtmember`
--

CREATE TABLE `mtmember` (
  `userid` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `notelp` varchar(255) DEFAULT NULL,
  `mail` varchar(2) NOT NULL,
  `totalamt` double DEFAULT NULL,
  `aktif` varchar(1) NOT NULL,
  `inuserid` varchar(255) DEFAULT NULL,
  `chuserid` varchar(255) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  `chdate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mtprice`
--

CREATE TABLE `mtprice` (
  `idprice` int(11) NOT NULL,
  `idcategory` int(11) NOT NULL,
  `idprod` int(11) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `iduom` int(11) DEFAULT NULL,
  `begdate` datetime NOT NULL,
  `enddate` datetime NOT NULL,
  `inuserid` varchar(255) DEFAULT NULL,
  `chuserid` varchar(255) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  `chdate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mtprod`
--

CREATE TABLE `mtprod` (
  `idprod` int(11) NOT NULL,
  `idcategory` int(11) NOT NULL,
  `nameprod` varchar(255) DEFAULT NULL,
  `iduom` int(11) DEFAULT NULL,
  `inuserid` varchar(255) DEFAULT NULL,
  `chuserid` varchar(255) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  `chdate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mtrole`
--

CREATE TABLE `mtrole` (
  `idrole` int(11) NOT NULL,
  `rolename` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mtrole`
--

INSERT INTO `mtrole` (`idrole`, `rolename`) VALUES
(1, 'role web/master'),
(2, 'role transactional'),
(3, 'role view/customer');

-- --------------------------------------------------------

--
-- Table structure for table `mtuom`
--

CREATE TABLE `mtuom` (
  `iduom` int(11) NOT NULL,
  `uomname` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `email` varchar(255) NOT NULL,
  `token` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `personal_access_tokens`
--

CREATE TABLE `personal_access_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tokenable_type` varchar(255) NOT NULL,
  `tokenable_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `token` varchar(64) NOT NULL,
  `abilities` text DEFAULT NULL,
  `last_used_at` timestamp NULL DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `trgrbres`
--

CREATE TABLE `trgrbres` (
  `idgrb` int(11) NOT NULL,
  `userid` varchar(255) NOT NULL,
  `idcategory` int(11) NOT NULL,
  `idprod` int(11) DEFAULT NULL,
  `qty` double DEFAULT NULL,
  `iduom` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `pricetot` decimal(10,2) DEFAULT NULL,
  `inuserid` varchar(255) DEFAULT NULL,
  `chuserid` varchar(255) DEFAULT NULL,
  `indate` datetime DEFAULT NULL,
  `chdate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `userid` varchar(225) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(225) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `remember_token` varchar(100) DEFAULT NULL,
  `role` varchar(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `failed_jobs_uuid_unique` (`uuid`);

--
-- Indexes for table `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mtcategory`
--
ALTER TABLE `mtcategory`
  ADD PRIMARY KEY (`idcategory`);

--
-- Indexes for table `mtprice`
--
ALTER TABLE `mtprice`
  ADD PRIMARY KEY (`idprice`);

--
-- Indexes for table `mtprod`
--
ALTER TABLE `mtprod`
  ADD PRIMARY KEY (`idprod`);

--
-- Indexes for table `mtrole`
--
ALTER TABLE `mtrole`
  ADD PRIMARY KEY (`idrole`);

--
-- Indexes for table `mtuom`
--
ALTER TABLE `mtuom`
  ADD PRIMARY KEY (`iduom`);

--
-- Indexes for table `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`email`);

--
-- Indexes for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `personal_access_tokens_token_unique` (`token`),
  ADD KEY `personal_access_tokens_tokenable_type_tokenable_id_index` (`tokenable_type`,`tokenable_id`);

--
-- Indexes for table `trgrbres`
--
ALTER TABLE `trgrbres`
  ADD PRIMARY KEY (`idgrb`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `failed_jobs`
--
ALTER TABLE `failed_jobs`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `mtcategory`
--
ALTER TABLE `mtcategory`
  MODIFY `idcategory` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mtprice`
--
ALTER TABLE `mtprice`
  MODIFY `idprice` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mtprod`
--
ALTER TABLE `mtprod`
  MODIFY `idprod` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mtrole`
--
ALTER TABLE `mtrole`
  MODIFY `idrole` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `mtuom`
--
ALTER TABLE `mtuom`
  MODIFY `iduom` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `personal_access_tokens`
--
ALTER TABLE `personal_access_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
