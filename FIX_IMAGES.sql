-- ========================================================
-- Script para corregir imágenes de productos
-- ========================================================

-- 1. Ver productos actuales con sus imágenes
SELECT id, name, image_url, category_id 
FROM products 
ORDER BY name;

-- 2. Actualizar imágenes de productos con error
-- Combo Kids
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1562227379-eb68a2ab4c77?w=400'
WHERE name = 'Combo Kids';

-- Perro New York
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1612392062798-2dd8f4b76813?w=400'
WHERE name = 'Perro New York';

-- Perro Ranchero  
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1599190913613-0c6ec8cbfea8?w=400'
WHERE name = 'Perro Ranchero';

-- 3. Verificar los cambios
SELECT id, name, image_url 
FROM products 
WHERE name IN ('Combo Kids', 'Perro New York', 'Perro Ranchero');

-- 4. (Opcional) Si hay más productos sin imagen o con imagen rota
-- Actualizar todos los productos que tengan URLs rotas o vacías con una imagen por defecto
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=400'
WHERE image_url IS NULL OR image_url = '' OR image_url LIKE '%placeholder%';
