# Guía para Consumir la API REST desde C#

## 📋 Tabla de Contenidos
1. [Modelo de Datos en C#](#modelo-de-datos)
2. [Servicio HTTP para consumir la API](#servicio-http)
3. [Aplicación de Consola](#aplicación-de-consola)
4. [Windows Forms Application](#windows-forms)
5. [WPF Application](#wpf-application)

---

## 1. Modelo de Datos en C# 📦

Primero, crea la clase `Producto.cs` que coincida con el modelo de tu API:

```csharp
using System;
using System.Text.Json.Serialization;

namespace IPhoneAPI.Models
{
    public class Producto
    {
        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("modelo")]
        public string Modelo { get; set; }

        [JsonPropertyName("precio")]
        public decimal Precio { get; set; }

        [JsonPropertyName("almacenamiento")]
        public string Almacenamiento { get; set; }

        [JsonPropertyName("color")]
        public string Color { get; set; }

        [JsonPropertyName("stock")]
        public int Stock { get; set; }

        [JsonPropertyName("fechaCreacion")]
        public DateTime? FechaCreacion { get; set; }

        public override string ToString()
        {
            return $"ID: {Id} | {Modelo} | {Almacenamiento} | {Color} | ${Precio} | Stock: {Stock}";
        }
    }
}
```

---

## 2. Servicio HTTP para consumir la API 🌐

Crea la clase `ProductoService.cs`:

```csharp
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using IPhoneAPI.Models;

namespace IPhoneAPI.Services
{
    public class ProductoService
    {
        private readonly HttpClient _httpClient;
        private readonly string _baseUrl = "http://localhost:8080/api/productos";

        public ProductoService()
        {
            _httpClient = new HttpClient();
            _httpClient.Timeout = TimeSpan.FromSeconds(30);
        }

        // GET - Obtener todos los productos
        public async Task<List<Producto>> ObtenerTodosAsync()
        {
            try
            {
                var response = await _httpClient.GetAsync(_baseUrl);
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<List<Producto>>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al obtener productos: {ex.Message}");
            }
        }

        // GET - Obtener producto por ID
        public async Task<Producto> ObtenerPorIdAsync(int id)
        {
            try
            {
                var response = await _httpClient.GetAsync($"{_baseUrl}/{id}");
                if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    return null;
                }
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<Producto>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al obtener producto: {ex.Message}");
            }
        }

        // POST - Crear nuevo producto
        public async Task<Producto> CrearAsync(Producto producto)
        {
            try
            {
                var response = await _httpClient.PostAsJsonAsync(_baseUrl, producto);
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<Producto>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al crear producto: {ex.Message}");
            }
        }

        // PUT - Actualizar producto
        public async Task<Producto> ActualizarAsync(int id, Producto producto)
        {
            try
            {
                var response = await _httpClient.PutAsJsonAsync($"{_baseUrl}/{id}", producto);
                if (response.StatusCode == System.Net.HttpStatusCode.NotFound)
                {
                    throw new Exception("Producto no encontrado");
                }
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<Producto>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al actualizar producto: {ex.Message}");
            }
        }

        // DELETE - Eliminar producto
        public async Task<bool> EliminarAsync(int id)
        {
            try
            {
                var response = await _httpClient.DeleteAsync($"{_baseUrl}/{id}");
                return response.IsSuccessStatusCode;
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al eliminar producto: {ex.Message}");
            }
        }

        // GET - Buscar por modelo
        public async Task<List<Producto>> BuscarPorModeloAsync(string modelo)
        {
            try
            {
                var response = await _httpClient.GetAsync($"{_baseUrl}/buscar/modelo/{modelo}");
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<List<Producto>>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al buscar por modelo: {ex.Message}");
            }
        }

        // GET - Buscar por color
        public async Task<List<Producto>> BuscarPorColorAsync(string color)
        {
            try
            {
                var response = await _httpClient.GetAsync($"{_baseUrl}/buscar/color/{color}");
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<List<Producto>>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al buscar por color: {ex.Message}");
            }
        }

        // GET - Buscar por almacenamiento
        public async Task<List<Producto>> BuscarPorAlmacenamientoAsync(string almacenamiento)
        {
            try
            {
                var response = await _httpClient.GetAsync($"{_baseUrl}/buscar/almacenamiento/{almacenamiento}");
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<List<Producto>>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al buscar por almacenamiento: {ex.Message}");
            }
        }

        // GET - Obtener productos disponibles
        public async Task<List<Producto>> ObtenerDisponiblesAsync()
        {
            try
            {
                var response = await _httpClient.GetAsync($"{_baseUrl}/disponibles");
                response.EnsureSuccessStatusCode();
                return await response.Content.ReadFromJsonAsync<List<Producto>>();
            }
            catch (Exception ex)
            {
                throw new Exception($"Error al obtener productos disponibles: {ex.Message}");
            }
        }
    }
}
```

---

## 3. Aplicación de Consola 💻

```csharp
using System;
using System.Threading.Tasks;
using IPhoneAPI.Models;
using IPhoneAPI.Services;

namespace IPhoneAPI.ConsoleApp
{
    class Program
    {
        static async Task Main(string[] args)
        {
            var service = new ProductoService();
            bool salir = false;

            Console.WriteLine("=== GESTIÓN DE PRODUCTOS IPHONE ===\n");

            while (!salir)
            {
                Console.WriteLine("\n--- MENÚ ---");
                Console.WriteLine("1. Listar todos los productos");
                Console.WriteLine("2. Buscar producto por ID");
                Console.WriteLine("3. Crear nuevo producto");
                Console.WriteLine("4. Actualizar producto");
                Console.WriteLine("5. Eliminar producto");
                Console.WriteLine("6. Buscar por modelo");
                Console.WriteLine("7. Productos disponibles");
                Console.WriteLine("0. Salir");
                Console.Write("\nSeleccione una opción: ");

                var opcion = Console.ReadLine();

                try
                {
                    switch (opcion)
                    {
                        case "1":
                            await ListarProductos(service);
                            break;
                        case "2":
                            await BuscarPorId(service);
                            break;
                        case "3":
                            await CrearProducto(service);
                            break;
                        case "4":
                            await ActualizarProducto(service);
                            break;
                        case "5":
                            await EliminarProducto(service);
                            break;
                        case "6":
                            await BuscarPorModelo(service);
                            break;
                        case "7":
                            await ProductosDisponibles(service);
                            break;
                        case "0":
                            salir = true;
                            Console.WriteLine("\n¡Hasta luego!");
                            break;
                        default:
                            Console.WriteLine("\nOpción inválida");
                            break;
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"\nError: {ex.Message}");
                }

                if (!salir)
                {
                    Console.WriteLine("\nPresione una tecla para continuar...");
                    Console.ReadKey();
                    Console.Clear();
                }
            }
        }

        static async Task ListarProductos(ProductoService service)
        {
            Console.WriteLine("\n--- LISTA DE PRODUCTOS ---");
            var productos = await service.ObtenerTodosAsync();
            
            if (productos.Count == 0)
            {
                Console.WriteLine("No hay productos registrados.");
                return;
            }

            foreach (var p in productos)
            {
                Console.WriteLine(p);
            }
        }

        static async Task BuscarPorId(ProductoService service)
        {
            Console.Write("\nIngrese el ID del producto: ");
            if (int.TryParse(Console.ReadLine(), out int id))
            {
                var producto = await service.ObtenerPorIdAsync(id);
                if (producto != null)
                {
                    Console.WriteLine($"\nProducto encontrado:\n{producto}");
                }
                else
                {
                    Console.WriteLine("\nProducto no encontrado.");
                }
            }
            else
            {
                Console.WriteLine("\nID inválido.");
            }
        }

        static async Task CrearProducto(ProductoService service)
        {
            Console.WriteLine("\n--- CREAR NUEVO PRODUCTO ---");
            
            Console.Write("Modelo: ");
            string modelo = Console.ReadLine();
            
            Console.Write("Precio: ");
            if (!decimal.TryParse(Console.ReadLine(), out decimal precio))
            {
                Console.WriteLine("Precio inválido.");
                return;
            }
            
            Console.Write("Almacenamiento (ej: 128GB): ");
            string almacenamiento = Console.ReadLine();
            
            Console.Write("Color: ");
            string color = Console.ReadLine();
            
            Console.Write("Stock: ");
            if (!int.TryParse(Console.ReadLine(), out int stock))
            {
                Console.WriteLine("Stock inválido.");
                return;
            }

            var nuevoProducto = new Producto
            {
                Modelo = modelo,
                Precio = precio,
                Almacenamiento = almacenamiento,
                Color = color,
                Stock = stock
            };

            var productoCreado = await service.CrearAsync(nuevoProducto);
            Console.WriteLine($"\n✓ Producto creado exitosamente con ID: {productoCreado.Id}");
        }

        static async Task ActualizarProducto(ProductoService service)
        {
            Console.Write("\nIngrese el ID del producto a actualizar: ");
            if (!int.TryParse(Console.ReadLine(), out int id))
            {
                Console.WriteLine("ID inválido.");
                return;
            }

            var productoExistente = await service.ObtenerPorIdAsync(id);
            if (productoExistente == null)
            {
                Console.WriteLine("\nProducto no encontrado.");
                return;
            }

            Console.WriteLine($"\nProducto actual: {productoExistente}");
            Console.WriteLine("\n--- NUEVOS DATOS (Enter para mantener) ---");
            
            Console.Write($"Modelo [{productoExistente.Modelo}]: ");
            string modelo = Console.ReadLine();
            if (!string.IsNullOrWhiteSpace(modelo)) productoExistente.Modelo = modelo;
            
            Console.Write($"Precio [{productoExistente.Precio}]: ");
            string precioStr = Console.ReadLine();
            if (decimal.TryParse(precioStr, out decimal precio)) productoExistente.Precio = precio;
            
            Console.Write($"Almacenamiento [{productoExistente.Almacenamiento}]: ");
            string almacenamiento = Console.ReadLine();
            if (!string.IsNullOrWhiteSpace(almacenamiento)) productoExistente.Almacenamiento = almacenamiento;
            
            Console.Write($"Color [{productoExistente.Color}]: ");
            string color = Console.ReadLine();
            if (!string.IsNullOrWhiteSpace(color)) productoExistente.Color = color;
            
            Console.Write($"Stock [{productoExistente.Stock}]: ");
            string stockStr = Console.ReadLine();
            if (int.TryParse(stockStr, out int stock)) productoExistente.Stock = stock;

            await service.ActualizarAsync(id, productoExistente);
            Console.WriteLine("\n✓ Producto actualizado exitosamente.");
        }

        static async Task EliminarProducto(ProductoService service)
        {
            Console.Write("\nIngrese el ID del producto a eliminar: ");
            if (!int.TryParse(Console.ReadLine(), out int id))
            {
                Console.WriteLine("ID inválido.");
                return;
            }

            Console.Write("¿Está seguro? (S/N): ");
            if (Console.ReadLine()?.ToUpper() == "S")
            {
                bool eliminado = await service.EliminarAsync(id);
                if (eliminado)
                {
                    Console.WriteLine("\n✓ Producto eliminado exitosamente.");
                }
                else
                {
                    Console.WriteLine("\nNo se pudo eliminar el producto.");
                }
            }
        }

        static async Task BuscarPorModelo(ProductoService service)
        {
            Console.Write("\nIngrese el modelo a buscar: ");
            string modelo = Console.ReadLine();
            
            var productos = await service.BuscarPorModeloAsync(modelo);
            
            Console.WriteLine($"\n--- RESULTADOS ({productos.Count}) ---");
            foreach (var p in productos)
            {
                Console.WriteLine(p);
            }
        }

        static async Task ProductosDisponibles(ProductoService service)
        {
            Console.WriteLine("\n--- PRODUCTOS CON STOCK DISPONIBLE ---");
            var productos = await service.ObtenerDisponiblesAsync();
            
            if (productos.Count == 0)
            {
                Console.WriteLine("No hay productos disponibles.");
                return;
            }

            foreach (var p in productos)
            {
                Console.WriteLine(p);
            }
        }
    }
}
```

---

## 4. Windows Forms Application 🖥️

### Form1.Designer.cs (Diseño del formulario)

```csharp
namespace IPhoneAPI.WinForms
{
    partial class Form1
    {
        private System.ComponentModel.IContainer components = null;
        private System.Windows.Forms.DataGridView dgvProductos;
        private System.Windows.Forms.Button btnCargar;
        private System.Windows.Forms.Button btnNuevo;
        private System.Windows.Forms.Button btnEditar;
        private System.Windows.Forms.Button btnEliminar;
        private System.Windows.Forms.Button btnBuscar;
        private System.Windows.Forms.TextBox txtBuscar;
        private System.Windows.Forms.GroupBox grpDatos;
        private System.Windows.Forms.TextBox txtId;
        private System.Windows.Forms.TextBox txtModelo;
        private System.Windows.Forms.TextBox txtPrecio;
        private System.Windows.Forms.TextBox txtAlmacenamiento;
        private System.Windows.Forms.TextBox txtColor;
        private System.Windows.Forms.TextBox txtStock;
        private System.Windows.Forms.Button btnGuardar;
        private System.Windows.Forms.Button btnCancelar;
        private System.Windows.Forms.Label lblId;
        private System.Windows.Forms.Label lblModelo;
        private System.Windows.Forms.Label lblPrecio;
        private System.Windows.Forms.Label lblAlmacenamiento;
        private System.Windows.Forms.Label lblColor;
        private System.Windows.Forms.Label lblStock;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        private void InitializeComponent()
        {
            this.dgvProductos = new System.Windows.Forms.DataGridView();
            this.btnCargar = new System.Windows.Forms.Button();
            this.btnNuevo = new System.Windows.Forms.Button();
            this.btnEditar = new System.Windows.Forms.Button();
            this.btnEliminar = new System.Windows.Forms.Button();
            this.btnBuscar = new System.Windows.Forms.Button();
            this.txtBuscar = new System.Windows.Forms.TextBox();
            this.grpDatos = new System.Windows.Forms.GroupBox();
            this.txtId = new System.Windows.Forms.TextBox();
            this.txtModelo = new System.Windows.Forms.TextBox();
            this.txtPrecio = new System.Windows.Forms.TextBox();
            this.txtAlmacenamiento = new System.Windows.Forms.TextBox();
            this.txtColor = new System.Windows.Forms.TextBox();
            this.txtStock = new System.Windows.Forms.TextBox();
            this.btnGuardar = new System.Windows.Forms.Button();
            this.btnCancelar = new System.Windows.Forms.Button();
            this.lblId = new System.Windows.Forms.Label();
            this.lblModelo = new System.Windows.Forms.Label();
            this.lblPrecio = new System.Windows.Forms.Label();
            this.lblAlmacenamiento = new System.Windows.Forms.Label();
            this.lblColor = new System.Windows.Forms.Label();
            this.lblStock = new System.Windows.Forms.Label();
            
            ((System.ComponentModel.ISupportInitialize)(this.dgvProductos)).BeginInit();
            this.grpDatos.SuspendLayout();
            this.SuspendLayout();

            // dgvProductos
            this.dgvProductos.AllowUserToAddRows = false;
            this.dgvProductos.AllowUserToDeleteRows = false;
            this.dgvProductos.AutoSizeColumnsMode = System.Windows.Forms.DataGridViewAutoSizeColumnsMode.Fill;
            this.dgvProductos.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dgvProductos.Location = new System.Drawing.Point(12, 50);
            this.dgvProductos.Name = "dgvProductos";
            this.dgvProductos.ReadOnly = true;
            this.dgvProductos.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.dgvProductos.Size = new System.Drawing.Size(760, 250);
            this.dgvProductos.TabIndex = 0;

            // txtBuscar
            this.txtBuscar.Location = new System.Drawing.Point(12, 15);
            this.txtBuscar.Name = "txtBuscar";
            this.txtBuscar.Size = new System.Drawing.Size(200, 23);
            this.txtBuscar.TabIndex = 1;

            // btnBuscar
            this.btnBuscar.Location = new System.Drawing.Point(220, 14);
            this.btnBuscar.Name = "btnBuscar";
            this.btnBuscar.Size = new System.Drawing.Size(75, 25);
            this.btnBuscar.TabIndex = 2;
            this.btnBuscar.Text = "Buscar";
            this.btnBuscar.UseVisualStyleBackColor = true;
            this.btnBuscar.Click += new System.EventHandler(this.btnBuscar_Click);

            // btnCargar
            this.btnCargar.Location = new System.Drawing.Point(310, 14);
            this.btnCargar.Name = "btnCargar";
            this.btnCargar.Size = new System.Drawing.Size(100, 25);
            this.btnCargar.TabIndex = 3;
            this.btnCargar.Text = "Cargar Todos";
            this.btnCargar.UseVisualStyleBackColor = true;
            this.btnCargar.Click += new System.EventHandler(this.btnCargar_Click);

            // btnNuevo
            this.btnNuevo.Location = new System.Drawing.Point(12, 310);
            this.btnNuevo.Name = "btnNuevo";
            this.btnNuevo.Size = new System.Drawing.Size(75, 30);
            this.btnNuevo.TabIndex = 4;
            this.btnNuevo.Text = "Nuevo";
            this.btnNuevo.UseVisualStyleBackColor = true;
            this.btnNuevo.Click += new System.EventHandler(this.btnNuevo_Click);

            // btnEditar
            this.btnEditar.Location = new System.Drawing.Point(93, 310);
            this.btnEditar.Name = "btnEditar";
            this.btnEditar.Size = new System.Drawing.Size(75, 30);
            this.btnEditar.TabIndex = 5;
            this.btnEditar.Text = "Editar";
            this.btnEditar.UseVisualStyleBackColor = true;
            this.btnEditar.Click += new System.EventHandler(this.btnEditar_Click);

            // btnEliminar
            this.btnEliminar.Location = new System.Drawing.Point(174, 310);
            this.btnEliminar.Name = "btnEliminar";
            this.btnEliminar.Size = new System.Drawing.Size(75, 30);
            this.btnEliminar.TabIndex = 6;
            this.btnEliminar.Text = "Eliminar";
            this.btnEliminar.UseVisualStyleBackColor = true;
            this.btnEliminar.Click += new System.EventHandler(this.btnEliminar_Click);

            // grpDatos
            this.grpDatos.Controls.Add(this.lblStock);
            this.grpDatos.Controls.Add(this.lblColor);
            this.grpDatos.Controls.Add(this.lblAlmacenamiento);
            this.grpDatos.Controls.Add(this.lblPrecio);
            this.grpDatos.Controls.Add(this.lblModelo);
            this.grpDatos.Controls.Add(this.lblId);
            this.grpDatos.Controls.Add(this.btnCancelar);
            this.grpDatos.Controls.Add(this.btnGuardar);
            this.grpDatos.Controls.Add(this.txtStock);
            this.grpDatos.Controls.Add(this.txtColor);
            this.grpDatos.Controls.Add(this.txtAlmacenamiento);
            this.grpDatos.Controls.Add(this.txtPrecio);
            this.grpDatos.Controls.Add(this.txtModelo);
            this.grpDatos.Controls.Add(this.txtId);
            this.grpDatos.Location = new System.Drawing.Point(12, 350);
            this.grpDatos.Name = "grpDatos";
            this.grpDatos.Size = new System.Drawing.Size(760, 200);
            this.grpDatos.TabIndex = 7;
            this.grpDatos.TabStop = false;
            this.grpDatos.Text = "Datos del Producto";
            this.grpDatos.Visible = false;

            // lblId
            this.lblId.AutoSize = true;
            this.lblId.Location = new System.Drawing.Point(20, 30);
            this.lblId.Name = "lblId";
            this.lblId.Size = new System.Drawing.Size(21, 15);
            this.lblId.TabIndex = 0;
            this.lblId.Text = "ID:";

            // txtId
            this.txtId.Enabled = false;
            this.txtId.Location = new System.Drawing.Point(120, 27);
            this.txtId.Name = "txtId";
            this.txtId.ReadOnly = true;
            this.txtId.Size = new System.Drawing.Size(100, 23);
            this.txtId.TabIndex = 1;

            // lblModelo
            this.lblModelo.AutoSize = true;
            this.lblModelo.Location = new System.Drawing.Point(20, 60);
            this.lblModelo.Name = "lblModelo";
            this.lblModelo.Size = new System.Drawing.Size(51, 15);
            this.lblModelo.TabIndex = 2;
            this.lblModelo.Text = "Modelo:";

            // txtModelo
            this.txtModelo.Location = new System.Drawing.Point(120, 57);
            this.txtModelo.Name = "txtModelo";
            this.txtModelo.Size = new System.Drawing.Size(300, 23);
            this.txtModelo.TabIndex = 3;

            // lblPrecio
            this.lblPrecio.AutoSize = true;
            this.lblPrecio.Location = new System.Drawing.Point(20, 90);
            this.lblPrecio.Name = "lblPrecio";
            this.lblPrecio.Size = new System.Drawing.Size(43, 15);
            this.lblPrecio.TabIndex = 4;
            this.lblPrecio.Text = "Precio:";

            // txtPrecio
            this.txtPrecio.Location = new System.Drawing.Point(120, 87);
            this.txtPrecio.Name = "txtPrecio";
            this.txtPrecio.Size = new System.Drawing.Size(150, 23);
            this.txtPrecio.TabIndex = 5;

            // lblAlmacenamiento
            this.lblAlmacenamiento.AutoSize = true;
            this.lblAlmacenamiento.Location = new System.Drawing.Point(20, 120);
            this.lblAlmacenamiento.Name = "lblAlmacenamiento";
            this.lblAlmacenamiento.Size = new System.Drawing.Size(100, 15);
            this.lblAlmacenamiento.TabIndex = 6;
            this.lblAlmacenamiento.Text = "Almacenamiento:";

            // txtAlmacenamiento
            this.txtAlmacenamiento.Location = new System.Drawing.Point(120, 117);
            this.txtAlmacenamiento.Name = "txtAlmacenamiento";
            this.txtAlmacenamiento.Size = new System.Drawing.Size(150, 23);
            this.txtAlmacenamiento.TabIndex = 7;

            // lblColor
            this.lblColor.AutoSize = true;
            this.lblColor.Location = new System.Drawing.Point(450, 60);
            this.lblColor.Name = "lblColor";
            this.lblColor.Size = new System.Drawing.Size(39, 15);
            this.lblColor.TabIndex = 8;
            this.lblColor.Text = "Color:";

            // txtColor
            this.txtColor.Location = new System.Drawing.Point(520, 57);
            this.txtColor.Name = "txtColor";
            this.txtColor.Size = new System.Drawing.Size(200, 23);
            this.txtColor.TabIndex = 9;

            // lblStock
            this.lblStock.AutoSize = true;
            this.lblStock.Location = new System.Drawing.Point(450, 90);
            this.lblStock.Name = "lblStock";
            this.lblStock.Size = new System.Drawing.Size(39, 15);
            this.lblStock.TabIndex = 10;
            this.lblStock.Text = "Stock:";

            // txtStock
            this.txtStock.Location = new System.Drawing.Point(520, 87);
            this.txtStock.Name = "txtStock";
            this.txtStock.Size = new System.Drawing.Size(100, 23);
            this.txtStock.TabIndex = 11;

            // btnGuardar
            this.btnGuardar.Location = new System.Drawing.Point(520, 150);
            this.btnGuardar.Name = "btnGuardar";
            this.btnGuardar.Size = new System.Drawing.Size(100, 30);
            this.btnGuardar.TabIndex = 12;
            this.btnGuardar.Text = "Guardar";
            this.btnGuardar.UseVisualStyleBackColor = true;
            this.btnGuardar.Click += new System.EventHandler(this.btnGuardar_Click);

            // btnCancelar
            this.btnCancelar.Location = new System.Drawing.Point(630, 150);
            this.btnCancelar.Name = "btnCancelar";
            this.btnCancelar.Size = new System.Drawing.Size(100, 30);
            this.btnCancelar.TabIndex = 13;
            this.btnCancelar.Text = "Cancelar";
            this.btnCancelar.UseVisualStyleBackColor = true;
            this.btnCancelar.Click += new System.EventHandler(this.btnCancelar_Click);

            // Form1
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(784, 561);
            this.Controls.Add(this.grpDatos);
            this.Controls.Add(this.btnEliminar);
            this.Controls.Add(this.btnEditar);
            this.Controls.Add(this.btnNuevo);
            this.Controls.Add(this.btnCargar);
            this.Controls.Add(this.btnBuscar);
            this.Controls.Add(this.txtBuscar);
            this.Controls.Add(this.dgvProductos);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Gestión de Productos iPhone";
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.dgvProductos)).EndInit();
            this.grpDatos.ResumeLayout(false);
            this.grpDatos.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();
        }
    }
}
```

### Form1.cs (Código del formulario)

```csharp
using System;
using System.Windows.Forms;
using IPhoneAPI.Models;
using IPhoneAPI.Services;

namespace IPhoneAPI.WinForms
{
    public partial class Form1 : Form
    {
        private ProductoService _service;
        private bool _esNuevo = false;

        public Form1()
        {
            InitializeComponent();
            _service = new ProductoService();
        }

        private async void Form1_Load(object sender, EventArgs e)
        {
            await CargarProductos();
        }

        private async System.Threading.Tasks.Task CargarProductos()
        {
            try
            {
                var productos = await _service.ObtenerTodosAsync();
                dgvProductos.DataSource = productos;
                ConfigurarDataGridView();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al cargar productos: {ex.Message}", "Error", 
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void ConfigurarDataGridView()
        {
            if (dgvProductos.Columns.Count > 0)
            {
                dgvProductos.Columns["Id"].HeaderText = "ID";
                dgvProductos.Columns["Id"].Width = 50;
                dgvProductos.Columns["Modelo"].HeaderText = "Modelo";
                dgvProductos.Columns["Precio"].HeaderText = "Precio";
                dgvProductos.Columns["Precio"].DefaultCellStyle.Format = "C2";
                dgvProductos.Columns["Almacenamiento"].HeaderText = "Almacenamiento";
                dgvProductos.Columns["Color"].HeaderText = "Color";
                dgvProductos.Columns["Stock"].HeaderText = "Stock";
                dgvProductos.Columns["FechaCreacion"].HeaderText = "Fecha Creación";
                dgvProductos.Columns["FechaCreacion"].DefaultCellStyle.Format = "dd/MM/yyyy HH:mm";
            }
        }

        private async void btnCargar_Click(object sender, EventArgs e)
        {
            await CargarProductos();
        }

        private async void btnBuscar_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtBuscar.Text))
            {
                await CargarProductos();
                return;
            }

            try
            {
                var productos = await _service.BuscarPorModeloAsync(txtBuscar.Text);
                dgvProductos.DataSource = productos;
                ConfigurarDataGridView();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al buscar: {ex.Message}", "Error", 
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void btnNuevo_Click(object sender, EventArgs e)
        {
            _esNuevo = true;
            LimpiarFormulario();
            grpDatos.Visible = true;
            txtModelo.Focus();
        }

        private void btnEditar_Click(object sender, EventArgs e)
        {
            if (dgvProductos.SelectedRows.Count == 0)
            {
                MessageBox.Show("Seleccione un producto para editar.", "Aviso", 
                    MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            _esNuevo = false;
            var producto = (Producto)dgvProductos.SelectedRows[0].DataBoundItem;
            CargarDatosFormulario(producto);
            grpDatos.Visible = true;
        }

        private async void btnEliminar_Click(object sender, EventArgs e)
        {
            if (dgvProductos.SelectedRows.Count == 0)
            {
                MessageBox.Show("Seleccione un producto para eliminar.", "Aviso", 
                    MessageBoxButtons.OK, MessageBoxIcon.Information);
                return;
            }

            var producto = (Producto)dgvProductos.SelectedRows[0].DataBoundItem;
            
            var result = MessageBox.Show($"¿Está seguro de eliminar el producto '{producto.Modelo}'?", 
                "Confirmar eliminación", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
            
            if (result == DialogResult.Yes)
            {
                try
                {
                    await _service.EliminarAsync(producto.Id);
                    MessageBox.Show("Producto eliminado exitosamente.", "Éxito", 
                        MessageBoxButtons.OK, MessageBoxIcon.Information);
                    await CargarProductos();
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"Error al eliminar: {ex.Message}", "Error", 
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            }
        }

        private async void btnGuardar_Click(object sender, EventArgs e)
        {
            if (!ValidarFormulario())
                return;

            try
            {
                var producto = new Producto
                {
                    Modelo = txtModelo.Text,
                    Precio = decimal.Parse(txtPrecio.Text),
                    Almacenamiento = txtAlmacenamiento.Text,
                    Color = txtColor.Text,
                    Stock = int.Parse(txtStock.Text)
                };

                if (_esNuevo)
                {
                    await _service.CrearAsync(producto);
                    MessageBox.Show("Producto creado exitosamente.", "Éxito", 
                        MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
                else
                {
                    producto.Id = int.Parse(txtId.Text);
                    await _service.ActualizarAsync(producto.Id, producto);
                    MessageBox.Show("Producto actualizado exitosamente.", "Éxito", 
                        MessageBoxButtons.OK, MessageBoxIcon.Information);
                }

                grpDatos.Visible = false;
                await CargarProductos();
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al guardar: {ex.Message}", "Error", 
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            grpDatos.Visible = false;
            LimpiarFormulario();
        }

        private bool ValidarFormulario()
        {
            if (string.IsNullOrWhiteSpace(txtModelo.Text))
            {
                MessageBox.Show("Ingrese el modelo del producto.", "Validación", 
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                txtModelo.Focus();
                return false;
            }

            if (!decimal.TryParse(txtPrecio.Text, out decimal precio) || precio <= 0)
            {
                MessageBox.Show("Ingrese un precio válido.", "Validación", 
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                txtPrecio.Focus();
                return false;
            }

            if (string.IsNullOrWhiteSpace(txtAlmacenamiento.Text))
            {
                MessageBox.Show("Ingrese el almacenamiento.", "Validación", 
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                txtAlmacenamiento.Focus();
                return false;
            }

            if (string.IsNullOrWhiteSpace(txtColor.Text))
            {
                MessageBox.Show("Ingrese el color.", "Validación", 
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                txtColor.Focus();
                return false;
            }

            if (!int.TryParse(txtStock.Text, out int stock) || stock < 0)
            {
                MessageBox.Show("Ingrese un stock válido.", "Validación", 
                    MessageBoxButtons.OK, MessageBoxIcon.Warning);
                txtStock.Focus();
                return false;
            }

            return true;
        }

        private void LimpiarFormulario()
        {
            txtId.Clear();
            txtModelo.Clear();
            txtPrecio.Clear();
            txtAlmacenamiento.Clear();
            txtColor.Clear();
            txtStock.Clear();
        }

        private void CargarDatosFormulario(Producto producto)
        {
            txtId.Text = producto.Id.ToString();
            txtModelo.Text = producto.Modelo;
            txtPrecio.Text = producto.Precio.ToString();
            txtAlmacenamiento.Text = producto.Almacenamiento;
            txtColor.Text = producto.Color;
            txtStock.Text = producto.Stock.ToString();
        }
    }
}
```

---

## 5. Pasos para crear el proyecto en Visual Studio 📝

### Opción 1: Consola

```bash
# 1. Crear proyecto de consola
dotnet new console -n IPhoneAPI.ConsoleApp

# 2. Navegar al proyecto
cd IPhoneAPI.ConsoleApp

# 3. Agregar paquete System.Net.Http.Json
dotnet add package System.Net.Http.Json

# 4. Compilar y ejecutar
dotnet run
```

### Opción 2: Windows Forms

```bash
# 1. Crear proyecto Windows Forms
dotnet new winforms -n IPhoneAPI.WinForms

# 2. Navegar al proyecto
cd IPhoneAPI.WinForms

# 3. Agregar paquete
dotnet add package System.Net.Http.Json

# 4. Compilar y ejecutar
dotnet run
```

---

## 📌 Notas Importantes

1. **Asegúrate de que la API esté ejecutándose** en `http://localhost:8080` antes de ejecutar la aplicación C#.

2. **Requisitos**:
   - .NET 6.0 o superior
   - Visual Studio 2022 o VS Code

3. **Paquete necesario**: `System.Net.Http.Json` (incluido en .NET 5+)

4. **CORS**: La API ya tiene `@CrossOrigin(origins = "*")` configurado, así que no habrá problemas de CORS.

---

## 🚀 ¿Listo para empezar?

1. Inicia tu API Spring Boot
2. Crea el proyecto C# que prefieras (Consola o WinForms)
3. Copia el código correspondiente
4. ¡Ejecuta y prueba!
