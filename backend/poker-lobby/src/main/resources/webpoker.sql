-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 21-01-2019 a las 15:36:12
-- Versión del servidor: 10.1.37-MariaDB
-- Versión de PHP: 7.0.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `webpoker`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `conversations`
--

CREATE TABLE `conversations` (
  `id_conversation` bigint(20) NOT NULL,
  `user_origin` bigint(20) NOT NULL,
  `user_target` bigint(20) NOT NULL,
  `last_message` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Friendships`
--

CREATE TABLE `Friendships` (
  `id_requester` bigint(20) NOT NULL,
  `id_target` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `request_date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `messages`
--

CREATE TABLE `messages` (
  `id_message` bigint(20) NOT NULL,
  `id_conversation` bigint(20) NOT NULL,
  `id_user_sender` bigint(20) NOT NULL,
  `date_sended` datetime NOT NULL,
  `readed` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rooms`
--

CREATE TABLE `rooms` (
  `id_server` bigint(20) NOT NULL,
  `server_identity_hash` varchar(250) NOT NULL,
  `ip` varchar(250) NOT NULL,
  `port` int(11) NOT NULL,
  `blind` bigint(20) NOT NULL,
  `min_bet` bigint(20) NOT NULL,
  `server_type` int(11) NOT NULL,
  `limit_bet` int(11) NOT NULL,
  `official_server` tinyint(1) NOT NULL,
  `server_name` varchar(250) NOT NULL,
  `players` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id_usuario` bigint(20) NOT NULL,
  `nick` varchar(250) NOT NULL,
  `email` varchar(250) NOT NULL,
  `password` varchar(250) NOT NULL,
  `signup_date` datetime NOT NULL,
  `last_activity` datetime NOT NULL,
  `coins` bigint(20) NOT NULL,
  `hashSignature` varchar(250) NOT NULL,
  `id_user_recommend` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `conversations`
--
ALTER TABLE `conversations`
  ADD PRIMARY KEY (`id_conversation`),
  ADD KEY `user_origin` (`user_origin`),
  ADD KEY `user_target` (`user_target`);

--
-- Indices de la tabla `Friendships`
--
ALTER TABLE `Friendships`
  ADD PRIMARY KEY (`id_requester`,`id_target`);

--
-- Indices de la tabla `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id_message`),
  ADD KEY `id_conversation` (`id_conversation`),
  ADD KEY `id_user_sender` (`id_user_sender`);

--
-- Indices de la tabla `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id_server`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `conversations`
--
ALTER TABLE `conversations`
  MODIFY `id_conversation` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `messages`
--
ALTER TABLE `messages`
  MODIFY `id_message` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id_server` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id_usuario` bigint(20) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
