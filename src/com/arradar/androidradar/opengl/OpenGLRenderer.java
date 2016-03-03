package com.arradar.androidradar.opengl;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.arradar.androidradar.RadarPlayer;
import com.arradar.androidradar.Settings;

public class OpenGLRenderer implements Renderer
{
	private Square square;
	private Circle circle;
	private FilledCircle filledCircle;
	private Triangle triangle;
	private Line line;
	
	private long startTime;
	private long endTime;
	
	private Settings settings;
	private RadarPlayer[] radarPlayers;
	
	public OpenGLRenderer(RadarPlayer[] radarPlayers, Settings settings)
	{
		this.radarPlayers = radarPlayers;
		this.settings = settings;
		
		startTime = System.currentTimeMillis();
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glClearColor(0.166f, 0.166f, 0.166f, 0.5f);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		square = new Square();
		circle = new Circle();
		filledCircle = new FilledCircle();
		triangle = new Triangle(); 
		line = new Line();
	}

	public void onDrawFrame(GL10 gl)
	{
		endTime = System.currentTimeMillis();
	    long dt = endTime - startTime;
	    if (dt < 33)
	    {
			try
			{
				Thread.sleep(33 - dt);
			} 
			catch (InterruptedException e) { }
	    }
	    startTime = System.currentTimeMillis();
		
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -10); 
		
		gl.glColor4f(0.52f, 0.52f, 0.52f, 0.5f);
		if (settings.radarBackgroundSkin == 1)
		{
			for (float pos = 4.0f; pos >= -4.0f; pos -= 1.0f)
			{
				line.draw(gl, -4.0f, pos, 4.0f, pos);
				line.draw(gl, pos, -4.0f, pos, 4.0f);
			}
		}
		else if (settings.radarBackgroundSkin == 2)
		{
			for (float wh = 5.0f; wh > 0.0f; wh -= 1.0f)
			{
				drawCircle(gl, 0, 0, wh, wh);
			}
		}
		else if (settings.radarBackgroundSkin == 3)
		{
			for (float wh = 5.0f; wh > 0.0f; wh -= 1.0f)
			{
				drawCircle(gl, 0, 0, wh, wh);
			}
			gl.glColor4f(0.0f, 0.52f, 0.0f, 0.5f);
			
			line.draw(gl, 0.0f, 0.0f, 3.0f, 4.0f);
			line.draw(gl, 0.0f, 0.0f, -3.0f, 4.0f);
		}
		else if (settings.radarBackgroundSkin == 4)
		{
			for (float wh = 5.0f; wh > 0.0f; wh -= 1.0f)
			{
				drawCircle(gl, 0, 0, wh, wh);
			}
			gl.glColor4f(0.0f, 0.52f, 0.0f, 0.5f);
			
			line.draw(gl, 0.0f, 0.0f, 3.0f, 4.0f);
			line.draw(gl, 0.0f, 0.0f, -3.0f, 4.0f);
			line.draw(gl, 0.0f, 0.0f, 0.0f, 5.0f);
		}
		gl.glColor4f(0, 0.52f, 0, 0.5f);
		drawFilledCircle(gl, 0, 0, 0.075f, 0.075f);
		
		for (int i = radarPlayers.length - 1; i >= 0; --i)
		{
			RadarPlayer radarPlayer = radarPlayers[i];
			if (radarPlayer.isValid)
			{
				if (radarPlayer.isEnemy)
				{	
					if (settings.radarShowHealth)
					{
						drawHealth(gl, radarPlayer.x, radarPlayer.y, radarPlayer.health);
					}
					gl.glColor4f(1.0f, 0.12f, 0.0f, 1.0f);
				} 
				else 
				{
					if (settings.radarShowFriendly)
					{
						if (settings.radarShowHealth)
						{
							drawHealth(gl, radarPlayer.x, radarPlayer.y, radarPlayer.health);
						}
						gl.glColor4f(0.016f,  0.671f,0.952f, 1.0f);
					}
					else
						continue;
				}
				
				if (settings.radarPlayerSkin == 0)
				{
					drawRectangle(gl, radarPlayer.x, radarPlayer.y, 0.1f, 0.1f);
				}
				else if (settings.radarPlayerSkin == 1)
				{
					drawFilledCircle(gl, radarPlayer.x, radarPlayer.y, 0.1f, 0.1f);
				}
				else
				{
					drawTriangle(gl, radarPlayer.x, radarPlayer.y, 0.1f, 0.1f, radarPlayer.yaw);
				} 
			}
		}
	}
	
	private void drawRectangle(GL10 gl, float x, float y, float width, float height)
	{
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);
		gl.glScalef(width, height, 0.5f);
		square.draw(gl);
		gl.glPopMatrix();
	}
	
	private void drawHealth(GL10 gl, float x, float y, int health)
	{
		gl.glColor4f(0.0f,  0.0f, 0.0f, 1.0f);
		float maxWidth = (float)(1.0f / 3.0f);
		float healthPercent = (float)(health / 300.0f);
		 
		drawRectangle(gl, x, y - 0.12f, maxWidth + 0.02f, 0.07f);
		if (health > 40)
		{
			gl.glColor4f(0.0f,  0.883f, 0.0f, 1.0f);
			drawRectangle(gl, x - (maxWidth - healthPercent), y - 0.122f, healthPercent, 0.05f);
		}
		else
		{
			gl.glColor4f(0.87f,  0.1f, 0.0f, 1.0f);
			drawRectangle(gl, x - (maxWidth - healthPercent), y - 0.122f, healthPercent, 0.05f);
		}
	}
	
	private void drawCircle(GL10 gl, float x, float y, float width, float height)
	{
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);
		gl.glScalef(width, height, width);
		circle.draw(gl);
		gl.glPopMatrix();
	}
	
	private void drawFilledCircle(GL10 gl, float x, float y, float width, float height)
	{
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);
		gl.glScalef(width, height, width);
		filledCircle.draw(gl);
		gl.glPopMatrix();
	}
	
	private void drawTriangle(GL10 gl, float x, float y, float width, float height, float angle)
	{
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);
		gl.glRotatef(angle, 0.0f, 0.0f, 1.0f); 
		gl.glScalef(width, height, 0.5f);
		triangle.draw(gl);
		gl.glPopMatrix();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		int newWidth, newHeight;
		int posX, posY;
		
		if (width > height)
		{
			newWidth = height;
			newHeight = height;
			posX = (width - height) / 2;
			posY = 0;
		}
		else
		{
			newWidth = width;
			newHeight = width;
			posX = 0;
			posY = (height - width) / 2;
		}
		
		gl.glViewport(posX, posY, newWidth, newHeight);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float)newWidth / (float)newHeight, 0.1f, 100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}