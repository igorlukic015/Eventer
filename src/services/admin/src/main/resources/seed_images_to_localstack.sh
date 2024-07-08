#!/bin/bash

BUCKET_NAME=eventer-images
IMAGE_FOLDER=./seed_images/

for image in $IMAGE_FOLDER/*; do
    if [[ -f $image ]]; then
        awslocal s3 cp "$image" s3://$BUCKET_NAME/
    fi
done
