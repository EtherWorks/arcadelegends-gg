package gg.al.game.ui;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ShortArray;

public class ProgressCircle extends Image {

	//ProgressCirlce là một hình được cắt bỏ một góc được tạo bởi đoạn thẳng từ tâm tới giữa cạnh  và "cây kim đồng hồ" quay quanh tâm.
	//"cây kim đồng hồ" ban đầu khi percent = 0 sẽ là đoạn thẳng từ tâm tới giữa cạnh trên.
	//và quay từ 0 đền 360 độ ứng với percent từ 0 đến 100
	
	//Hằng số ghi nhận "cây kim đồng hồ" cắt cạnh nào của imgae.
	public enum IntersectAt {
		 NONE, TOP, BOTTOM, LEFT, RIGHT;
		}
	
	//region của hình.
	TextureRegion texture;
	
	//polyBatch để vẽ một phần hình của image. 
	PolygonSpriteBatch polyBatch;
	
	//các điểm đại diện cho tâm, giữa đỉnh trên, left-top, left-bottom, right-top, right-buttom.. và điểm "kim đồng hồ" cắt vào canh. 
	Vector2 center;
	Vector2 centerTop;
	Vector2 leftTop;
	Vector2 leftBottom;
	Vector2 rightBottom;
	Vector2 rightTop;
	Vector2 progressPoint;
	
	//danh sách các điểm cần vẽ...
	float[] fv;
	
	//cờ cho biết "kim đồng hồ" đã cắt canh nào của image..
	IntersectAt intersectAt;
	
	
	//Hàm khởi tạo - có tham số là region của hình và PolygonSpriteBatch để vẽ....
	public ProgressCircle(TextureRegion region, PolygonSpriteBatch polyBatch)
	{
		super(region);
		
		this.texture = region;
		this.polyBatch = polyBatch;
		
		//Định nghĩa các điểm đại diện...	
		center = new Vector2(this.getWidth()/2, this.getHeight()/2);
		centerTop = new Vector2(this.getWidth()/2, this.getHeight());
		leftTop = new Vector2(0, this.getHeight());
		leftBottom = new Vector2(0, 0);
		rightBottom = new Vector2(this.getWidth(), 0);
		rightTop = new Vector2(this.getWidth(), this.getHeight());
		progressPoint = new Vector2(this.getWidth()/2, this.getHeight()/2);
		
		//khởi đầu cho percent = 0;
		setPercentage(0);
		
		
	}

	
	//Hàm tính "cây kim đồng hổ" (line) - cắt với 1 trong 4 cạnh của imgae.
	//hàm trả về điểm cắt và cập nhật cạnh được cắt...
	private Vector2 IntersectPoint(Vector2 line)
	{
		Vector2 v = new Vector2();
		boolean isIntersect;
		
		//check top
		isIntersect = Intersector.intersectSegments(leftTop, rightTop, center, line, v);

		//check bottom
		if (isIntersect) { intersectAt = IntersectAt.TOP; return v; } 
		else isIntersect = Intersector.intersectSegments(leftBottom, rightBottom, center, line, v);
		
		//check left
		if (isIntersect) { intersectAt = IntersectAt.BOTTOM; return v; } 
		else isIntersect = Intersector.intersectSegments(leftTop, leftBottom, center, line, v);

		//check bottom
		if (isIntersect) { intersectAt = IntersectAt.LEFT; return v; } 
		else isIntersect = Intersector.intersectSegments(rightTop, rightBottom, center, line, v);
		
		if (isIntersect) { intersectAt = IntersectAt.RIGHT; return v; } 
		else
		{
			intersectAt = IntersectAt.NONE; 
			return null;
		}
	}
	
	
	//cho giá trị percent để quay "kim đồng hồ"
	public void setPercentage(float percent)
	{
		//100 % = 360 degree
		//==> percent % => (percent * 360 / 100) degree 
		
		float angle = convertToRadians(90); //percent = 0 => angle = -90
		angle -= convertToRadians(percent * 360 / 100);
		
		float len = this.getWidth() > this.getHeight() ? this.getWidth() : this.getHeight();
	    float dy = (float) (Math.sin(angle) * len);
	    float dx = (float) (Math.cos(angle) * len);
		Vector2 line = new Vector2(center.x + dx, center.y + dy);
	    
		Vector2 v = IntersectPoint(line);
		
		if (intersectAt == IntersectAt.TOP)
		{
			if (v.x >= this.getWidth()/2) // cắt bên phải cạnh 
			{
				//các điểm sẽ vẽ sẽ là ....
				fv = new float[] {
						center.x,
						center.y,
						centerTop.x,
						centerTop.y,
						leftTop.x,
						leftTop.y,
						leftBottom.x,
						leftBottom.y,
						rightBottom.x,
						rightBottom.y,
						rightTop.x,
						rightTop.y,
						v.x,
						v.y
						};
			}
			else
			{
				fv = new float[] { // cắt bên trái cạnh 
						center.x,
						center.y,
						centerTop.x,
						centerTop.y,
						v.x,
						v.y
						};
				
			}
		}
		else if (intersectAt == IntersectAt.BOTTOM)
		{
			fv = new float[] {
					center.x,
					center.y,
					centerTop.x,
					centerTop.y,
					leftTop.x,
					leftTop.y,
					leftBottom.x,
					leftBottom.y,
					v.x,
					v.y
					};
			
		}
		else if (intersectAt == IntersectAt.LEFT)
		{
			fv = new float[] {
					center.x,
					center.y,
					centerTop.x,
					centerTop.y,
					leftTop.x,
					leftTop.y,
					v.x,
					v.y
					};
			
		}
		else if (intersectAt == IntersectAt.RIGHT)
		{
			fv = new float[] {
					center.x,
					center.y,
					centerTop.x,
					centerTop.y,
					leftTop.x,
					leftTop.y,
					leftBottom.x,
					leftBottom.y,
					rightBottom.x,
					rightBottom.y,
					v.x,
					v.y
					};
			
		}
		else // if (intersectAt == IntersectAt.NONE)
		{
			//không cắt thì không vẽ........:-) 
			fv = null;
		}
		
		
	}
	
	
	
	// override hàm draw để tự vẽ theo cách riêng của mình...

	public void draw(SpriteBatch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
		
		if (fv == null) return;
		
		batch.end(); //nhớ tắt batch để vẽ bằng polyPatch. 
		drawMe();
		batch.begin(); //nhớ mở lại như cũ...
		
	}
	
	
	//Vẽ một phần của hình (có bỏ góc quay của kim đồng hồ)....theo fv
	public void drawMe()
	{
		//quy chiếu các điểm tạo polygon sang triangula (tham biến thứ hai crua polygonRegion).
		EarClippingTriangulator e = new EarClippingTriangulator();
		ShortArray sv = e.computeTriangles(fv);
		
		//tạo polygonRegion.
		PolygonRegion polyReg = new PolygonRegion( texture, fv, sv.toArray());
		
		//tạo polySprite từ polygonRegion.
		PolygonSprite poly = new PolygonSprite(polyReg);

		//lấy tất cả thuộc tính của image cho poly (position, origin, rotation, color)
		poly.setOrigin(this.getOriginX(), this.getOriginY());
		poly.setPosition(this.getX(), this.getY());
		poly.setRotation(this.getRotation());
		poly.setColor(this.getColor());
		
		//bắt đầu vẽ poly (với đầy đủ thuộc tính của Image).
	    polyBatch.begin();
	    poly.draw(polyBatch);
	    polyBatch.end();

		
	}
	
	
	
//-----------------------------------------------------------------	

	
	float convertToDegrees(float angleInRadians)
	{
	    float angleInDegrees = angleInRadians * 57.2957795f;
	    return angleInDegrees;
	}

	float convertToRadians(float angleInDegrees)
	{
	    float angleInRadians = angleInDegrees * 0.0174532925f;
	    return angleInRadians;
	}
	
	
	
}
