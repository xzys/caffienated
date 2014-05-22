var game = new Phaser.Game(640, 640, Phaser.CANVAS, "game_div");

var players;

var tilemap;
var layer;
var tiles = {};

var cursors;

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
		}

		cursors = game.input.keyboard.createCursorKeys();
		
	},

	update: function() {
		// TODO only ones that are in vicinity;
		for(var i = 0;i < players.length;i++) {

			// which tile the player is in
			var check_tiles = [];
			
			var px = players[0].x + players[0].deltaX;
			var py = players[0].y + players[0].deltaY;
			var tilex = Math.floor(px / 32);
			var tiley = Math.floor(py / 32);
			var kx = px % 32 > 16 ? 1 : -1;
			var ky = py % 32 > 16 ? 1 : -1;

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
		if (cursors.left.isDown) {
	        players[0].body.moveLeft(20);
	    }
	    else if (cursors.right.isDown) {
	        players[0].body.moveRight(20);
	    }

	    if (cursors.up.isDown && players[0].body.touching.down) {
	        players[0].body.moveUp(20);
	    }
	    else if (cursors.down.isDown) {
	        players[0].body.moveDown(20);
	    }
	}
}

game.state.add('main', main_state);
game.state.start('main');