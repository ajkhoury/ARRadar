package com.arradar.androidradar.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Triangle 
{
	private FloatBuffer vertexBuffer = null;
	private ShortBuffer indexBuffer = null;
	private int numOfIndices = 0;
	
	private float vertexList[] = {
	   								-1.0f, -1.0f, 0.0f, 	//left
	   								1.00f, -1.0f, 0.0f, 	//right
	   								0.00f, 1.40f, 0.0f		//top
								};
	
	private short indexList[] = { 0, 1, 2 };

	public Triangle() 
	{	 
    	// Set vertex buffer
	     ByteBuffer vbb = ByteBuffer.allocateDirect(vertexList.length * 4); 
	     vbb.order(ByteOrder.nativeOrder());
	     vertexBuffer = vbb.asFloatBuffer();
	     vertexBuffer.put(vertexList); 
	     vertexBuffer.position(0); 
 
	     // Set indices buffer
	     numOfIndices = indexList.length; 
	     ByteBuffer tbibb = ByteBuffer.allocateDirect(numOfIndices * 2); 
	     tbibb.order(ByteOrder.nativeOrder());
	     indexBuffer = tbibb.asShortBuffer();
	     indexBuffer.put(indexList); 
	     indexBuffer.position(0);
    }
	
	public void draw(GL10 gl)
	{
		gl.glFrontFace(GL10.GL_CCW);
		//gl.glEnable(GL10.GL_CULL_FACE);
		//gl.glCullFace(GL10.GL_BACK);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		//gl.glRotatef(26.0f, 0.0f, 1.0f, 0.0f);
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, numOfIndices, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		//gl.glDisable(GL10.GL_CULL_FACE);
	}
	
}
