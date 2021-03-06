/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package glengine.yan.glengine.util.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

public class YANTextureHelper {
    private static final String TAG = "TextureHelper";
    private static final boolean IS_LOGGING_ON = true;

    /**
     * Loads a texture from a resource ID, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param resourceId
     * @return 0 if the load failed.
     */
    public static int loadTexture(Context context, int resourceId) {
        final Bitmap bitmap = loadBitmapFromResources(context, resourceId);
        return loadBitmapIntoOpenGL(bitmap);
    }

    /**
     * Loads a texture from an assets folder with provided name and path, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param texturePath a full path starting from assets folder as a root with image name and extention.
     * @return 0 if the load failed.
     */
    public static int loadTexture(Context context, String texturePath) {
        final Bitmap bitmap = loadBitmapFromAssets(context, texturePath);
        return loadBitmapIntoOpenGL(bitmap);
    }

    private static Bitmap loadBitmapFromAssets(Context context, String texturePath) {
        Bitmap loadedBitmap = null;
        try {
            InputStream ims = context.getAssets().open(texturePath);
            loadedBitmap = BitmapFactory.decodeStream(ims);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return loadedBitmap;
    }

    private static int loadBitmapIntoOpenGL(Bitmap bitmap) {
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            if (IS_LOGGING_ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.");
            }
            return 0;
        }

        if (bitmap == null) {
            if (IS_LOGGING_ON) {
                Log.w(TAG, "Bitmap could not be decoded.");
            }

            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);

        // Set filtering: a default must be set, or the texture will be
        // black.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        // Load the bitmap into the bound texture.
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.

        glGenerateMipmap(GL_TEXTURE_2D);

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
        bitmap.recycle();

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0);

        return textureObjectIds[0];
    }

    private static Bitmap loadBitmapFromResources(Context context, int resourceId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        // Read in the resource
        return BitmapFactory.decodeResource(
                context.getResources(), resourceId, options);
    }

    public static void deleteTexture(Integer textureHandle) {
        final int[] textureObjectIds = new int[1];
        textureObjectIds[0] = textureHandle;
        GLES20.glDeleteTextures(1, textureObjectIds, 0);
    }
}
