module.exports = function (ctx) {
    var Q = ctx.requireCordovaModule("q");
    var fs = ctx.requireCordovaModule("fs");
    var path = ctx.requireCordovaModule("path");
    var deferral = Q.defer();

    // Android path: platforms/android/assets/www
    ctx.opts.platforms.forEach(function (platform) {
        if (platform === "android") {
            //handle Android Resource Files
            var platformPath = path.join(ctx.opts.projectRoot, "platforms/android");
            var configPath = path.join(platformPath,"/assets/www/jack35PluginResources");

            fs.readdir(configPath, function (err, files) {
                if (err) {
                    console.log("Failed to handle plugin resources: " + err);
                    deferral.resolve();
                    return;
                }
                copyFolderRecursiveSync(configPath,path.join(platformPath,"res/drawable"));
                deferral.resolve();
            });
        }
    });

    function copyFileSync(source, target) {

        var targetFile = target;

        //if target is a directory a new file with the same name will be created
        if (fs.existsSync(target)) {
            if (fs.lstatSync(target).isDirectory()) {
                targetFile = path.join(target, path.basename(source));
            }
        }

        fs.writeFileSync(targetFile, fs.readFileSync(source));
    }

    function copyFolderRecursiveSync(source, target) {
        var files = [];

        //check if folder needs to be created or integrated
        var targetFolder = path.join(target);
        if (!fs.existsSync(targetFolder)) {
            fs.mkdirSync(targetFolder);
        }

        //copy
        if (fs.lstatSync(source).isDirectory()) {
            files = fs.readdirSync(source);
            files.forEach(function (file) {
                var curSource = path.join(source, file);
                if (fs.lstatSync(curSource).isDirectory()) {
                    copyFolderRecursiveSync(curSource, targetFolder);
                } else {
                    copyFileSync(curSource, targetFolder);
                }
            });
        }
    }

    return deferral.promise;
};