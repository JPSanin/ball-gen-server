package model;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;

import processing.core.PApplet;

public class Main extends PApplet{
	private Socket socket;
	private BufferedWriter bw;
	private BufferedReader br;
	private ArrayList<Bolita> bolitas;

	public static void main(String[] args) {
		PApplet.main(Main.class.getName());
	}

	public void settings() {
		size(800,600);

	}

	public void setup() {
		initServer();
		bolitas= new ArrayList<>();
		
	}
	
	public void draw() {
		background(0);
		for(int i=0; i<bolitas.size();i++) {
			
			if(bolitas.get(i).getTipo()==1) {
			fill(200,0,0);
			}
			if(bolitas.get(i).getTipo()==2) {
			fill(0,200,0);	
			}
			if(bolitas.get(i).getTipo()==3) {
			fill(0,25,128);		
			}
			ellipse(bolitas.get(i).getX(),bolitas.get(i).getY(),50,50);
			bolitas.get(i).move();
			
			if(mouseX<bolitas.get(i).getX()+25 && mouseX>bolitas.get(i).getX()-25
			&& mouseY<bolitas.get(i).getY()+25 && mouseY>bolitas.get(i).getY()-25) {
			bolitas.get(i).setStop(true);	
			fill(255);
			textSize(20);
			textAlign(CENTER);
			text(bolitas.get(i).getGrupo(),bolitas.get(i).getX(),bolitas.get(i).getY()+25);
			}else {
				bolitas.get(i).setStop(false);
			}
		}
	
	}
	
	
	public void initServer() {
		new Thread(
				()->{
					
					try {
						ServerSocket server= new ServerSocket(5000);
						System.out.println("Waiting for client...");
						socket=server.accept();
						System.out.println("Client connected");
						
						
						//Send
						InputStream is= socket.getInputStream();
						InputStreamReader isr= new InputStreamReader(is);
						br= new BufferedReader(isr);
						
						OutputStream os = socket.getOutputStream();
						OutputStreamWriter osw= new OutputStreamWriter(os);
						bw= new BufferedWriter(osw);
						
						while(true) {
							System.out.println("Waiting...");
							String line = br.readLine(); 
							if(line.equals("delete")) {
								bolitas.clear();
								System.out.println("Deletion Completed");
								
							}else {
								Gson gson = new Gson();
								Bolita b= gson.fromJson(line, Bolita.class);
								bolitas.add(b);
								System.out.println("Recieved");
								System.out.println("Msg: "+ line);
							}
							
						}
						
						
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}).start();
		
	}
	
	
	public void sendMessage(String msg) {
		new Thread(
				()->{
					
					try {
						
						bw.write(msg+"\n");
						bw.flush();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					
				}).start();
	}
	
	public void mousePressed() {
		
	}
}
