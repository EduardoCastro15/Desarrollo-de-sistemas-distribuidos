
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class Cliente {

    public static void main(String[] args) throws Exception {

        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("\na. Alta usuario");
            System.out.println("b. Consulta usuario");
            System.out.println("c. Borra usuario");
            System.out.println("d. Salir");
            System.out.print("Opcion: ");

            char opc = br.readLine().charAt(0);

            switch (opc) {
                case 'a':
                    System.out.println("\n-----Alta usuario-----");
                    Usuario usuario = new Usuario();

                    System.out.print("Email: ");
                    usuario.email = br.readLine();

                    System.out.print("Nombre: ");
                    usuario.nombre = br.readLine();

                    System.out.print("Apellido Paterno: ");
                    usuario.apellido_paterno = br.readLine();

                    System.out.print("Apellido Materno: ");
                    usuario.apellido_materno = br.readLine();
                    
                    System.out.print("Fecha de nacimiento: ");
                    usuario.fecha_nacimiento = br.readLine();

                    System.out.print("Telefono: ");
                    usuario.telefono = br.readLine();

                    System.out.print("Genero (M/F): ");
                    usuario.genero = br.readLine();
                    alta_usuario(usuario);
                    break;
                case 'b':
                    System.out.println("-----Consulta usuario-----");
                    System.out.print("Ingresa el ID de usuario: ");
                    consultar_usuario(Integer.parseInt(br.readLine()));
                    break;
                case 'c':
                    System.out.println("-----Borrar usuario-----");
                    System.out.print("Ingresa el ID de usuario: ");
                    borrar_usuario(Integer.parseInt(br.readLine()));
                    break;
                case 'd':
                    br.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    
    public static void alta_usuario(Usuario usuario) throws IOException {
        URL url = new URL("http://20.121.12.169:8080/Servicio/rest/ws/alta_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        conexion.setRequestMethod("POST");

        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        Gson gson = builder.create();
        String body = gson.toJson(usuario);

        String parametros = "usuario=" + URLEncoder.encode(body, "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();

        if (conexion.getResponseCode() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;

            while ((respuesta = br.readLine()) != null)
                System.out.println("Se agrego el usuario con ID " + respuesta);
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        }

        conexion.disconnect();

    }

    public static void consultar_usuario(int id_usuario) throws IOException {

        URL url = new URL("http://20.121.12.169:8080/Servicio/rest/ws/consulta_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);


        conexion.setRequestMethod("POST");


        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String parametros = "id_usuario=" + URLEncoder.encode(String.valueOf(id_usuario), "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();
        Usuario user = null;
        
        if (conexion.getResponseCode() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            String respuesta;
            //Usuario user = null;
            Gson j = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            while ((respuesta = br.readLine()) != null){
                user = (Usuario) j.fromJson(respuesta, Usuario.class);
                System.out.println("ID: " + user.id_usuario);
                System.out.println("Email: " + user.email);
                System.out.println("Nombre: " + user.nombre);
                System.out.println("Apellido Paterno: " + user.apellido_paterno);
                System.out.println("Apellido Materno: " + user.apellido_materno);
                System.out.println("Fecha: " + user.fecha_nacimiento);
                System.out.println("Telefono: " + user.telefono);
                System.out.println("Genero: " + user.genero);
            }

            
                
           // BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            // System.out.print("Desea modificar los datos del usuario? (s/n): ");
            // String x = System.console().readLine();
            // System.out.println("Resp: "+x);
            // if (x.equals("s")){
            //     System.out.println("entro: ");
            //     Usuario usuarioAux = new Usuario();
                
            //     usuarioAux.id_usuario = user.id_usuario;
            //     System.out.print("Email: ");
            //     usuarioAux.email = br.readLine();

            //     if(usuarioAux.email == "") usuarioAux.email = user.email;

            //     System.out.print("Nombre: ");
            //     usuarioAux.nombre = br.readLine();

            //     if(usuarioAux.nombre == "") usuarioAux.nombre = user.nombre;

            //     System.out.print("Apellido Paterno: ");
            //     usuarioAux.apellido_paterno = br.readLine();

            //     if(usuarioAux.apellido_paterno == "") usuarioAux.apellido_paterno = user.apellido_paterno;

            //     System.out.print("Apellido Materno: ");
            //     usuarioAux.apellido_materno = br.readLine();

            //     if(usuarioAux.apellido_materno == "") usuarioAux.apellido_materno = user.apellido_materno;
                
            //     System.out.print("Fecha de nacimiento: ");
            //     usuarioAux.fecha_nacimiento = br.readLine();

            //     if(usuarioAux.fecha_nacimiento == "") usuarioAux.fecha_nacimiento = user.fecha_nacimiento;

            //     System.out.print("Telefono: ");
            //     usuarioAux.telefono = br.readLine();

            //     if(usuarioAux.telefono == "") usuarioAux.telefono = user.telefono;

            //     System.out.print("Genero (M/F): ");
            //     usuarioAux.genero = br.readLine();

            //     if(usuarioAux.genero == "") usuarioAux.genero = user.genero;

            //     modificar_usuario(usuarioAux);
            //}

        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        }

        conexion.disconnect();

        System.out.print("Desea modificar los datos del usuario? (s/n): ");
            String x = System.console().readLine();
            System.out.println("Resp: "+x);
            if (x.equals("s")){
                System.out.println("Nombre: " + user.nombre);
                System.out.println("\nIngresa los nuevos valores");
                Usuario usuarioAux = new Usuario();
                
                usuarioAux.id_usuario = user.id_usuario;
                System.out.print("Email: ");
                usuarioAux.email = System.console().readLine();

                if(usuarioAux.email.equals("")) usuarioAux.email = user.email;

                System.out.print("Nombre: ");
                usuarioAux.nombre = System.console().readLine();

                if(usuarioAux.nombre.equals("")) usuarioAux.nombre = user.nombre;

                System.out.print("Apellido Paterno: ");
                usuarioAux.apellido_paterno = System.console().readLine();

                if(usuarioAux.apellido_paterno.equals("")) usuarioAux.apellido_paterno = user.apellido_paterno;

                System.out.print("Apellido Materno: ");
                usuarioAux.apellido_materno = System.console().readLine();

                if(usuarioAux.apellido_materno.equals("")) usuarioAux.apellido_materno = user.apellido_materno;
                
                System.out.print("Fecha de nacimiento: ");
                usuarioAux.fecha_nacimiento = System.console().readLine();

                if(usuarioAux.fecha_nacimiento.equals("")) usuarioAux.fecha_nacimiento = user.fecha_nacimiento;

                System.out.print("Telefono: ");
                usuarioAux.telefono = System.console().readLine();

                if(usuarioAux.telefono.equals("")) usuarioAux.telefono = user.telefono;

                System.out.print("Genero (M/F): ");
                usuarioAux.genero = System.console().readLine();

                if(usuarioAux.genero.equals("")) usuarioAux.genero = user.genero;

                modificar_usuario(usuarioAux);
            }
    }

    public static void modificar_usuario(Usuario usuario) throws IOException {
        
        URL url = new URL("http://20.121.12.169:8080/Servicio/rest/ws/modifica_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);
        conexion.setRequestMethod("POST");
        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();

        Gson gson = builder.create();
        String body = gson.toJson(usuario);

        String parametros = "usuario=" + URLEncoder.encode(body, "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();

        if (conexion.getResponseCode() == 200) {
            System.out.println("El usuario ha sido modificado");
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        }

        conexion.disconnect();

    }

    public static void borrar_usuario(int id_usuario) throws IOException {

        URL url = new URL("http://20.121.12.169:8080/Servicio/rest/ws/borra_usuario");
        HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
        conexion.setDoOutput(true);

        conexion.setRequestMethod("POST");

        conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String parametros = "id_usuario=" + URLEncoder.encode(String.valueOf(id_usuario), "UTF-8");

        OutputStream os = conexion.getOutputStream();
        os.write(parametros.getBytes());

        os.flush();


        if (conexion.getResponseCode() == 200) {
            //BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getInputStream())));
            //String respuesta;

            //while ((respuesta = br.readLine()) != null)
                //System.out.println(respuesta);
            System.out.println("El usuario ha sido borrado");
        } else { // hubo error
            BufferedReader br = new BufferedReader(new InputStreamReader((conexion.getErrorStream())));
            String respuesta;
            while ((respuesta = br.readLine()) != null)
                System.out.println(respuesta);

            throw new RuntimeException("Codigo de error HTTP: " + conexion.getResponseCode());
        }

        conexion.disconnect();

    }
}

class Usuario {
    int id_usuario;
    String email;
    String nombre;
    String apellido_paterno;
    String apellido_materno;
    String fecha_nacimiento;
    String telefono;
    String genero;
    byte[] foto;
}

