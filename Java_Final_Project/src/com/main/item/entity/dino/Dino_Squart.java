package com.main.item.entity.dino;

import java.awt.Graphics;

import com.main.Game;
import com.main.GameParameter;
import com.main.gfx.Image;
import com.main.item.Handler;
import com.main.item.Id;
import com.main.item.entity.Entity;

public class Dino_Squart extends Entity {

	private final double player_jumping_height = 8.0; // 跳躍高度
	private final double jumping_speed = 0.3; // 跳躍高度
	protected double player_gravity = 0.8; // 下降重力

	public Dino_Squart(Id id, Handler handler, int x, int y, int width, int height) {
		super(id, handler, x, y, width, height);

		sheetLength = Dino_Stand_Run.dino_Stand_Run_Images.length;

		// 設定圖片，左上角是(1,1)，(sheet, x, y, 要讀幾個進來)
		for (int i = 0; i < sheetLength-3; i++)
			Dino_Stand_Run.dino_Stand_Run_Images[i + 3] = new Image(Game.imageSheet, i + 3 + 1, 1, Id.GET_DINO_SQUART);

		immutableSpeed = 64 * 2 / moveSpeedfloor1 + 1; // 無敵 (1/animation_speed) 秒，物體要通過2個長度的人物
		animation_speed = (60 / moveSpeedfloor1 > 0) ? 60 / moveSpeedfloor1 : 1; // 每 (1/animation_speed) 秒 變換一次動畫
	}

	@Override
	public void render(Graphics g) {
		/***** 設定圖片 *****/
		// 如果蹲下
		if (immutable == true && twinkling == true) {
			g.drawImage(Game.immutableSheet.getBufferedImage(), x, y, width, height, null);
			twinkling = false;
		} else {
			if (animation % 2 == 0)
				g.drawImage(Dino_Stand_Run.dino_Stand_Run_Images[3].getBufferedImage(), x, y, width, height, null);
			else if (animation % 2 == 1)
				g.drawImage(Dino_Stand_Run.dino_Stand_Run_Images[4].getBufferedImage(), x, y, width, height, null);
			twinkling = true;
		}
	}

	@Override
	public void update() {
		/***** 更新數據 *****/
		animation_speed = (60 / moveSpeedfloor1 > 0) ? 60 / moveSpeedfloor1 : 1; // 每 (1/animation_speed) 秒 變換一次動畫
		immutableSpeed = 64 * 2 / moveSpeedfloor1 + 1; // 無敵 (1/animation_speed) 秒，物體要通過2個長度的人物

		/***** 做事 *****/
		doCollidingDetection(); // 判斷碰撞
		doAnimation(); // 處理動畫
		do_check_Immutable(); // 處理無敵
	}

	@Override
	public void doKeyPressed1() {
		// 不做事
	}

	@Override
	public void doKeyPressed2() {
		Game.handler.addEntity(new Dino_Stand_Run(Id.Dino_Stand_Run, Game.handler, GameParameter.WIDTH / 7,
				GameParameter.HEIGHT * 1 / 4 - 53 - 32, 49, 53)); // 建立站著恐龍
		Game.handler.removeEntity(this);
	}

	public void jump() {
		if (jumping == true) {
			player_gravity -= jumping_speed;
			setVelY((int) -player_gravity);

			// 到達至高點的時候
			if (player_gravity <= 0.8) {
				jumping = false;
				falling = true;
				player_gravity = 0.8;
			}
		}

		// 下降的時候
		if (falling == true) {
			player_gravity += jumping_speed;
			setVelY((int) player_gravity);

			// 到達至低點的時候
			if (y >= getStart_y() || player_gravity >= player_jumping_height) {
				jumping = false;
				falling = false;
				velY = 0;
				y = getStart_y();
			}
		}
	}

	/*
	 * Getters and Setters
	 * 
	 */
	public double getJumping_speed() {
		return jumping_speed;
	}

}
