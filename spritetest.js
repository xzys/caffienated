var game = new Phaser.Game(640, 640, Phaser.CANVAS, "game_div");

var players;

var main_state = {
	preload : function() {
		// game.load.tilemap('map', 'assets/tilemap2.json', null, Phaser.Tilemap.TILED_JSON);
		// game.load.image('tiles', 'assets/ninja-tiles32.png');
		// game.load.image('player', 'assets/default.png');
		game.load.spritesheet('gorilla', 'assets/gorilla_walk.png', 68, 64);
	},

	create: function() {
		players = []
		players.push(game.add.sprite(100, 100, 'gorilla'));

		game.time.advancedTiming = true;
		cursors = game.input.keyboard.createCursorKeys();
		Phaser.Time.advancedTiming = true;
		

		players[0].animations.add('stand');
		players[0].animations.play('stand', 1, true);
	},

	update: function() {
	}
}

game.state.add('main', main_state);
game.state.start('main');
