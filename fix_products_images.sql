-- Script para corregir las URLs de imágenes de productos con error

-- Ver todos los productos actuales
SELECT id, name, image_url FROM products ORDER BY id;

-- Actualizar imágenes de productos específicos
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=400'
WHERE name LIKE '%Combo Kids%';

UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1612392062798-2dd8f4b76813?w=400'
WHERE name LIKE '%Perro New York%';

UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1599190913613-0c6ec8cbfea8?w=400'
WHERE name LIKE '%Perro Ranchero%';

-- Verificar los cambios
SELECT id, name, image_url, category_id FROM products WHERE name IN ('Combo Kids', 'Perro New York', 'Perro Ranchero');
