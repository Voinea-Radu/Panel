<div id="header">
    <div class="nav">
        <div class="container">
            <ul class="left">
                <li <?php if($page=='index'){?> class="active" <?php }?>><a href="index.php">Home</a></li>
                <li <?php if($page=='forums'){?> class="active" <?php }?>><a href="forums.php">Forums</a></li>
                <li <?php if($page=='stats'){?> class="active" <?php }?>><a href="stats.php">Stats</a></li>
                <li <?php if($page=='support'){?> class="active" <?php }?>><a href="support.php">Support</a></li>
                <li class="store"><a href="https://store.cavepvp.org/">Store</a></li>
            </ul>
            <div class="right">
                <form>
                    <i class="mdi mdi-magnify"></i>
                    <input name="username" placeholder="Username">
                </form>
                <a href="login.php" class="login-button">
                    <i class="mdi mdi-account-plus"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="header-content">
        <div class="container">
            <div class="col players" id="copy" data-clipboard-text="CAVEPVP.ORG">
                <i class="mdi mdi-minecraft"></i>
                <div class="text">
                    <h1>CAVEPVP.ORG <span class="count">0</span></h1>
                    <p>Click to copy</p>
                </div>
            </div>
            <div class="col logo">
                <a href="index.php">
                    <div id="logo">
                        <img src="https://i.imgur.com/oEyjQXC.png">
                        <img src="https://i.imgur.com/kfQR6TQ.png" class="special float1">
                        <img src="https://i.imgur.com/1z99c4P.png" class="special float2">
                        <img src="https://i.imgur.com/ao02w4D.png" class="special float3">
                    </div>
                </a>
            </div>
            <div class="col discord">
                <i class="mdi mdi-discord"></i>
                <div class="text">
                    <h1><span class="count">0</span> JOIN DISCORD</h1>
                    <p>Click to join</p>
                </div>
            </div>
        </div>
    </div>
</div>