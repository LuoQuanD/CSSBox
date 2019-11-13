/*
 * GraphicsEngine.java
 * Copyright (c) 2005-2019 Radek Burget
 *
 * CSSBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * CSSBox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with CSSBox. If not, see <http://www.gnu.org/licenses/>.
 *
 * Created on 13. 11. 2019, 15:05:30 by burgetr
 */
package org.fit.cssbox.layout;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.render.GraphicsRenderer;
import org.w3c.dom.Element;

/**
 * An implementation of the rendering engine using a Graphics2D renderer and
 * visual context. 
 *
 * @author burgetr
 */
public class GraphicsEngine extends Engine
{
    private boolean createImage;
    private BufferedImage img;
    private Graphics2D ig;

    
    public GraphicsEngine(Element root, DOMAnalyzer decoder, URL baseurl)
    {
        super(root, decoder, baseurl);
        this.createImage = true;
    }

    public GraphicsEngine(Element root, DOMAnalyzer decoder, Dimension dim, URL baseurl)
    {
        super(root, decoder, dim, baseurl);
        this.createImage = true;
    }

    /**
     * @return the graphics context for drawing in the page image
     */
    public Graphics2D getImageGraphics()
    {
        return img.createGraphics();
    }
    
    /**
     * @return image containing the rendered page
     */
    public BufferedImage getImage()
    {
        return img;
    }
    
    /**
     * Sets a custom image that is used for rendering. Setting the custom image prevents BrowserCanvas
     * from creating the image automatically. This can be used for rendering to an image of a specific
     * size or format.
     * @param image The new image to be used for rendering.
     */
    public void setImage(BufferedImage image)
    {
        img = image;
        ig = img.createGraphics();
        createImage = false;
    }

    //==========================================================================================================
    
    @Override
    protected void initOutputMedia(float width, float height)
    {
        if (createImage)
        {
            img = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
            ig = img.createGraphics();
        }
    }

    @Override
    protected VisualContext createVisualContext(BoxFactory factory)
    {
        VisualContext ctx = new GraphicsVisualContext(ig, null, factory);
        return ctx;
    }
    
    @Override
    protected void renderViewport(Viewport viewport)
    {
        GraphicsRenderer r = new GraphicsRenderer(ig); 
        r.clearCanvas(viewport);
        viewport.draw(r);
        r.close();
    }

    
    
}