-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.8-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping data for table ceceq.usuarios_permisos: ~0 rows (approximately)
/*!40000 ALTER TABLE `usuarios_permisos` DISABLE KEYS */;
INSERT INTO `usuarios_permisos` (`id`, `nombre`) VALUES
	(5, 'agregar usuario'),
	(7, 'deshabilitar usuario'),
	(9, 'deshabilitar usuario temporal'),
	(6, 'eliminar usuario'),
	(8, 'habilitar usuario'),
	(10, 'habilitar usuario temporal'),
	(11, 'ver dashboard'),
	(3, 'ver instalaciones'),
	(2, 'ver mapas'),
	(1, 'ver reportes'),
	(4, 'ver usuarios');
/*!40000 ALTER TABLE `usuarios_permisos` ENABLE KEYS */;

-- Dumping data for table ceceq.usuarios_roles: ~0 rows (approximately)
/*!40000 ALTER TABLE `usuarios_roles` DISABLE KEYS */;
INSERT INTO `usuarios_roles` (`id`, `nombre`) VALUES
	(2, 'administrador'),
	(1, 'moderador'),
	(3, 'superadministrador');
/*!40000 ALTER TABLE `usuarios_roles` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

-- Dumping data for table ceceq.usuarios_permisosroles: ~0 rows (approximately)
/*!40000 ALTER TABLE `usuarios_permisosroles` DISABLE KEYS */;
INSERT INTO `usuarios_permisosroles` (`id`, `id_permiso_id`, `id_rol_id`) VALUES
	(1, 11, 1),
	(2, 2, 1),
	(3, 3, 1),
	(4, 11, 2),
	(5, 2, 2),
	(6, 3, 2),
	(7, 1, 2),
	(8, 4, 2),
	(9, 9, 2),
	(10, 10, 2),
	(11, 11, 3),
	(12, 2, 3),
	(13, 3, 3),
	(14, 1, 3),
	(15, 4, 3),
	(16, 9, 3),
	(17, 10, 3),
	(18, 5, 3),
	(19, 6, 3),
	(20, 7, 3),
	(21, 8, 3);
/*!40000 ALTER TABLE `usuarios_permisosroles` ENABLE KEYS */;