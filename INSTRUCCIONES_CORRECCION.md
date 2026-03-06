# 🔧 Instrucciones para Corregir Imágenes y Filtros

## ✅ Problema 1: Filtro por Categoría - SOLUCIONADO

El filtro por categoría ahora funciona correctamente. Los cambios realizados:

1. **ProductController actualizado**: Agregado parámetro `category` para filtrar productos
2. **products.html actualizado**: Botones de categoría ahora se resaltan cuando están activos

**Cómo usar:**
- Haz clic en cualquier categoría (Hamburguesas, Bebidas, Postres, Entradas)
- El botón se pondrá azul sólido (en lugar de outline)
- Se mostrarán solo los productos de esa categoría

---

## 🖼️ Problema 2: Imágenes con Error

Para corregir las imágenes de **Combo Kids**, **Perro New York** y **Perro Ranchero**, sigue estos pasos:

### Opción 1: Usar pgAdmin (Recomendado)

1. Abre **pgAdmin**
2. Conéctate al servidor **ANDECUMO**
3. En el árbol de la izquierda, navega a:
   - ANDECUMO → Databases → postgres → Schemas → public → Tables → products
4. Haz clic derecho en **products** → **Query Tool**
5. Copia y pega el contenido del archivo **FIX_IMAGES.sql**
6. Haz clic en el botón **▶ Execute** (F5)
7. Verifica que las actualizaciones fueron exitosas

### Opción 2: Usar psql desde terminal

```powershell
# Conectarse a PostgreSQL
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d postgres

# Dentro de psql, ejecutar:
\i 'c:/Users/andecumo/OneDrive - colsubsidio.com/Documents/01 Universidad/PAGINA-WEB-_-LENGUAJE-PROGRAMACION/FIX_IMAGES.sql'

# O copiar y pegar las consultas manualmente
```

### Opción 3: Ejecutar comandos manualmente en pgAdmin Query Tool

```sql
-- Actualizar Combo Kids
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1562227379-eb68a2ab4c77?w=400'
WHERE name = 'Combo Kids';

-- Actualizar Perro New York
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1612392062798-2dd8f4b76813?w=400'
WHERE name = 'Perro New York';

-- Actualizar Perro Ranchero  
UPDATE products 
SET image_url = 'https://images.unsplash.com/photo-1599190913613-0c6ec8cbfea8?w=400'
WHERE name = 'Perro Ranchero';
```

---

## 🚀 Reiniciar la Aplicación

Después de actualizar las imágenes en la base de datos:

1. **Detener la aplicación actual** (si está corriendo):
   ```powershell
   # Presiona Ctrl+C en la terminal donde corre la aplicación
   ```

2. **Reiniciar la aplicación**:
   ```powershell
   ./mvnw spring-boot:run
   ```

3. **Verificar en el navegador**:
   - Abre: http://localhost:8080/products
   - Inicia sesión con `user` / `user123`
   - Verifica que:
     - ✅ Las imágenes se muestran correctamente
     - ✅ El filtro por categoría funciona
     - ✅ Los botones de categoría se resaltan al seleccionarlos

---

## 📝 Cambios Realizados

### Archivos Modificados:

1. **ProductController.java**
   - Agregado parámetro `category` en el método `products()`
   - Lógica para filtrar por categoría o mostrar todos los productos
   - Agregado atributo `selectedCategory` al modelo

2. **products.html**
   - Actualizado los enlaces de categorías para usar `th:href="@{/products(category=${cat.id})}"`
   - Agregado lógica para resaltar la categoría seleccionada con `th:classappend`
   - Mejora visual: categoría activa = `btn-primary`, inactiva = `btn-outline-primary`

3. **FIX_IMAGES.sql** (nuevo archivo)
   - Script SQL para actualizar las URLs de las imágenes rotas
   - Incluye consultas de verificación

---

## ✅ Verificación Final

```sql
-- Ejecutar en pgAdmin para verificar que las imágenes están correctas
SELECT id, name, image_url 
FROM products 
WHERE name IN ('Combo Kids', 'Perro New York', 'Perro Ranchero');
```

Si todas las URLs muestran enlaces de Unsplash, ¡todo está correcto! 🎉
