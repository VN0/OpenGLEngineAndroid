package glengine.yan.glengine.assets.atlas;

import android.content.Context;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import glengine.yan.glengine.assets.YANAssetDescriptor;
import glengine.yan.glengine.assets.YANAssetLoader;
import glengine.yan.glengine.assets.atlas.pojos.YANTexturePackerPojos;
import glengine.yan.glengine.util.helpers.YANTextFileHelper;

/**
 * Created by Yan-Home on 1/10/2015.
 */
public class YANTextureAtlasLoader implements YANAssetLoader<YANTextureAtlas> {

    private final Context mCtx;

    public YANTextureAtlasLoader(Context appContext) {
        mCtx = appContext;
    }

    @Override
    public YANTextureAtlas loadAsset(YANAssetDescriptor assetDescriptor) {
        //load json ui atlas descriptor
        String jsonAtlasString = YANTextFileHelper.readTextFileFromAssets(mCtx, assetDescriptor.getPathToAsset() + assetDescriptor.getAssetName() + "." + assetDescriptor.getAssetExtension());
        YANTexturePackerPojos.WrappingObject loadedPojo = (new Gson()).fromJson(jsonAtlasString, YANTexturePackerPojos.WrappingObject.class);

        //allocate texture atlas
        YANTextureAtlas atlas = new YANTextureAtlas(assetDescriptor.getPathToAsset() + loadedPojo.getMetaData().getAtlasImageFileName());

        //load texture regions
        Map<String, YANAtlasTextureRegion> textureRegionsMap = createTextureRegionsMap(loadedPojo, atlas);

        //assign texture regions to atlas
        atlas.setTextureRegions(textureRegionsMap);

        //atlas is ready
        return atlas;
    }

    private Map<String, YANAtlasTextureRegion> createTextureRegionsMap(YANTexturePackerPojos.WrappingObject loadedPojo, YANTextureAtlas atlas) {
        HashMap<String, YANAtlasTextureRegion> retMap = new HashMap<>();

        for (int i = 0; i < loadedPojo.getFramesList().size(); i++) {
            YANTexturePackerPojos.Frame frame = loadedPojo.getFramesList().get(i);
            retMap.put(frame.getTextureFileName(), createTextureRegionFromFrame(atlas, frame, loadedPojo.getMetaData().getAtlasImageSize()));
        }

        return retMap;
    }

    private YANAtlasTextureRegion createTextureRegionFromFrame(YANTextureAtlas atlas, YANTexturePackerPojos.Frame frame, YANTexturePackerPojos.AtlasImageSize atlasImageSize) {
        String regionName = frame.getTextureFileName();
        float u0 = frame.getFrameData().getX() / atlasImageSize.getW();
        float u1 = (frame.getFrameData().getX() + frame.getFrameData().getW()) / atlasImageSize.getW();
        float v0 = ((frame.getFrameData().getY()) / atlasImageSize.getH());
        float v1 = ((frame.getFrameData().getY() + frame.getFrameData().getH()) / atlasImageSize.getH());
        return new YANAtlasTextureRegion(atlas, regionName, u0, u1, v0, v1, frame.getFrameData().getW(), frame.getFrameData().getH());
    }

}