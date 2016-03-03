package com.arradar.androidradar.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Line
{
	private FloatBuffer vertexBuffer;
	// Our index buffer.
	private ShortBuffer indexBuffer;

	public Line()
	{
		ByteBuffer vbb = ByteBuffer.allocateDirect(2 * 3 * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();

		ByteBuffer ibb = ByteBuffer.allocateDirect(2 * 3 * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(new short[] { 0, 1, 2, 3, 4, 5 });
		
		indexBuffer.position(0);
	}

	/**
	 * This function draws our square on screen.
	 * @param gl
	 */
	public void draw(GL10 gl, float x1, float y1, float x2, float y2)
	{
		vertexBuffer.clear();
		vertexBuffer.put(x1);
		vertexBuffer.put(y1);
		vertexBuffer.put(0.0f);
		vertexBuffer.put(x2);
		vertexBuffer.put(y2);
		vertexBuffer.put(0.0f);
		vertexBuffer.position(0);
		
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW); // OpenGL docs
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE); // OpenGL docs
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK); // OpenGL docs

		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// OpenGL docs.
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glDrawElements(GL10.GL_LINE_LOOP, 2, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE); // OpenGL docs
	}
}
