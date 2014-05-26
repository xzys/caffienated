var game = new Phaser.Game(640, 640, Phaser.CANVAS, "game_div");

var players;

var tilemap;
var layer;
var tiles = {};

var cursors;

var fps;

var main_state = {
	preload : function() {
		game.load.tilemap('map', 'assets/tilemap.json', null, Phaser.Tilemap.TILED_JSON);
		game.load.image('tiles', 'assets/ninja-tiles32.png');
		game.load.image('player', 'assets/default.png');
	},

	create: function() {
		game.physics.startSystem(Phaser.Physics.NINJA);
		

		tilemap = game.add.tilemap('map');
		tilemap.addTilesetImage('ninja-tiles32', 'tiles');

		layer = tilemap.createLayer('Tile Layer 1');
		// layer.debug = true;
		layer.resizeWorld();
		tilemap.setCollisionBetween(1, 32);
		
		// physics tiles directly from the tilemap data
		var slopemap = { '2' : 1, '3' : 2 };
		var t = game.physics.ninja.convertTilemap(tilemap, layer, slopemap);

		// making a dictionary of tiles that do exist
		// because getTile returns a Phaesr.Tile, not a Phaser.Physics.Ninja.Tile
		for(var i=0;i < t.length;i++) {
			var gx = (t[i].x - 16) / 32;
			var gy = (t[i].y - 16) / 32;
			tiles[gx + "," + gy] = t[i].tile;
		}
		
		players = []
		players.push(game.add.sprite(50, 50, 'player'));
		
		for(var i = 0;i < players.length;i++) {
			game.physics.ninja.enableCircle(players[i], players[i].width / 2);
			players[i].body.bounce = 0;
			// additional 
			players[i]['state'] = STANDING;
		}
		players[0]['consts'] = {'run' : 15,
								'stop' : 8,
								'air_walk': 10,
								'max_vel' : 90,
								'deadzone' : 1,
								'jump' : 250,
								'wall_jump' : 100,
								'mid_jump' : 2}

		fps = game.add.text(10, 10, "", {font: '12px Courier New',
										    fill: '#fff' });
		
		game.time.advancedTiming = true;

		cursors = game.input.keyboard.createCursorKeys();
		Phaser.Time.advancedTiming = true;
	},

	update: function() {
		// TODO only ones that are in vicinity;
		fps.setText('FPS: ' + game.time.fps);
		
		for(var i = 0;i < players.length;i++) {

			// which tile the player is in
			
			px = players[0].x + players[0].deltaX;
			py = players[0].y + players[0].deltaY;
			tilex = Math.floor(px / 32);
			tiley = Math.floor(py / 32);
			kx = px % 32 > 16 ? 1 : -1;
			ky = py % 32 > 16 ? 1 : -1;

			check_tiles[0] = tiles[(tilex) + "," + (tiley)];
			check_tiles[1] = tiles[(tilex + kx) + "," + (tiley)];
			check_tiles[2] = tiles[(tilex) + "," + (tiley  + ky)];
			check_tiles[3] = tiles[(tilex + kx) + "," + (tiley + ky)];
			
			for(var j = 0;j < check_tiles.length; j++) {
				if(check_tiles[j]) {
					players[i].body.circle.collideCircleVsTile(check_tiles[j]);
				}
			}
		}

		// MOVEMENT
		processState(players[0]);
		// console.log(players[0].state);
	}
}
var px, py, tilex, tiley, kx, ky;
var check_tiles = [];
var STANDING = 0;
var MIDAIR = 1;
var JUMPING = 2;

// play animations as they are needed
function switchAnimations(player, anim) {

}

function processState(player) {
	switch(player.state) {
		case STANDING:
			if(player.body.touching.down || player.body.wasTouching.down) {
				if(cursors.up.isDown) {
					player.body.moveUp(player.consts.jump);
					player.state = JUMPING;
					return;
				}

				if(cursors.right.isDown) {
					if(player.body.velocity.x > 0) {
						// play running
						if(player.body.velocity.x < player.consts.max_vel) {
							player.body.moveRight(player.consts.run);
						}
					} else {
						// play skidding frame
						player.body.moveRight(player.consts.stop);
					}
				} else if(cursors.left.isDown) {
					if(player.body.velocity.x < 0) {
						// play running
						if(player.body.velocity.x > player.consts.max_vel * -1) {
							player.body.moveLeft(player.consts.run);
						}
					} else {
						// play skidding frame
						player.body.moveLeft(player.consts.run);
					}
				}

				// no need to apply impulse because physics takes care of it
				// just change animation frame, dont skid
				if(player.body.velocity.x > player.consts.deadzone) {
					// play skidding frame
				} else if(player.body.velocity.x < player.consts.deadzone * -1) {
					// play skidding frame left
				}
			} else {
				// falling
				player.state = MIDAIR;
			}
			break;
		case JUMPING:
			if(player.body.touching.none  || player.body.wasTouching.none) {
				// holding jump
				if(cursors.up.isDown) {
					player.body.moveUp(player.consts.mid_jump);
				} else {
					// cancel jump
					player.state = MIDAIR;
				}

				if(cursors.right.isDown) {
					player.body.moveRight(player.consts.air_walk);
				} else if(cursors.left.isDown) {
					player.body.moveLeft(player.consts.air_walk);
				}
				// no need to cancel time based because eventually vely == 0
			} else {
				player.state = MIDAIR;
			}
			break;
		case MIDAIR:
			if(player.body.touching.right || player.body.wasTouching.right) {
				if(cursors.right.isDown) {
					// setState(player, WALLRIDING;

					// set player friction here
					// play wallriding frame
				} else {
					// play falling frame
				}

				// walljump
				if(cursors.up.isDown) {
					player.body.moveUp(player.consts.wall_jump);
					player.body.moveLeft(player.consts.wall_jump);
				}

			} else if(player.body.touching.left || player.body.wasTouching.left) {
				if(cursors.left.isDown) {
					// setState(player, WALLRIDING;
					
					// set player friction here
					// play wallriding frame
				} else {
					// play falling frame
				}

				// walljump	
				if(cursors.up.isDown) {
					player.body.moveUp(player.consts.wall_jump);
					player.body.moveRight(player.consts.wall_jump);
					player.state = MIDAIR;
				}

			} else if(player.body.touching.down || player.body.wasTouching.down) {
				player.state = STANDING;
			} else {// really midair
				if(cursors.right.isDown) {
					player.body.moveRight(player.consts.air_walk);
				} else if(cursors.left.isDown) {
					player.body.moveLeft(player.consts.air_walk);
				}
			}
			break;
	}
}

function backup_process_state(player) {
	if (cursors.left.isDown) {
		players[0].body.moveLeft(20);
	}
	else if (cursors.right.isDown) {
		players[0].body.moveRight(20);
	}

	if (cursors.up.isDown) {
		players[0].body.moveUp(500);
	}
	else if (cursors.down.isDown) {
		players[0].body.moveDown(20);
	}
}

game.state.add('main', main_state);
game.state.start('main');
