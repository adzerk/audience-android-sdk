#!/usr/bin/env sh

# abort on errors
set -e

# build
yarn run dist

# navigate into the build output directory
cd temp/.vuepress/dist

# if you are deploying to a custom domain
echo 'android.developers.velocidi.com' > CNAME

git init
git add -A

git config user.email "baco@velocidi.com"
git config user.name "Baco"

git commit -m 'deploy'

# if you are deploying to https://<USERNAME>.github.io/<REPO>
git push -f git@github.com:velocidi/velocidi-android-sdk.git master:gh-pages

cd -
