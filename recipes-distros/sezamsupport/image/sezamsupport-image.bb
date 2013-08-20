DESCRIPTION = "Sezam Image"
SECTION = "base"
PRIORITY = "required"
LICENSE = "proprietary"
MAINTAINER = "Sezam support team"

require conf/license/license-gplv2.inc

PV = "${IMAGE_VERSION}"
PR = "r${DATETIME}"
PACKAGE_ARCH = "${MACHINE_ARCH}"

IMAGE_INSTALL = " \
    oe-alliance-base \
    sezamsupport-enigma2 \
    sezamsupport-bootlogo \
    sezamsupport-version-info \
    ${ENIGMA2_PLUGINS} \
    ${ENIGMA2_INI_PLUGINS} \
    ${@base_contains("MACHINE", "ventonhdx", "${ENIGMA2_USB_DRV}" , "", d)} \
    libcrypto-compat \
    ntfs-3g \
    hddtemp \
    busybox-cron \
    python-gdata \
    minidlna \
    task-base-smbfs \
    task-base-smbfs-client \
    mc \
    "

ENIGMA2_PLUGINS = "\
    enigma2-plugin-extensions-epgsearch \
    enigma2-plugin-extensions-imdb \
    enigma2-plugin-extensions-mediaplayer \
    enigma2-plugin-extensions-cutlisteditor \
    ${@base_contains("MACHINE", "ventonhdx", "enigma2-plugin-extensions-foreca" , "", d)} \
    enigma2-plugin-extensions-dlnabrowser \
    ${@base_contains("MACHINE", "ventonhdx", "enigma2-plugin-systemplugins-videotune" , "", d)} \
    enigma2-plugin-systemplugins-softwaremanager \
    enigma2-plugin-systemplugins-hotplug \
    ${@base_contains("MACHINE", "ventonhdx", "enigma2-plugin-systemplugins-3gmodemmanager" , "", d)} \
    enigma2-plugin-systemplugins-positionersetup \
    ${@base_contains("MACHINE", "ventonhdx", "enigma2-plugin-systemplugins-micomupgrade" , "", d)} \
"
ENIGMA2_INI_PLUGINS = "\
    ${@base_contains("MACHINE", "ventonhdx", "enigma2-plugin-picons-default-ventonsupport" , "", d)} \
    enigma2-plugin-extensions-inimytube \
"

ENIGMA2_USB_DRV = "\
    enigma2-plugin-drivers-dvb-usb-af9035 \
    enigma2-plugin-drivers-dvb-usb-dib0700 \
    enigma2-plugin-drivers-dvb-usb-af9015 \
    enigma2-plugin-drivers-dvb-usb-siano \
    enigma2-plugin-drivers-dvb-usb-dw2102 \
    enigma2-plugin-drivers-dvb-usb-as102 \
    enigma2-plugin-drivers-dvb-usb-dtt200u \
    enigma2-plugin-drivers-usbserial \
    enigma2-plugin-drivers-dvb-usb-dib0700 \
    enigma2-plugin-drivers-dvb-usb-af9015 \
    enigma2-plugin-drivers-dvb-usb-siano \
    enigma2-plugin-drivers-dvb-usb-em28xx  \
    enigma2-plugin-drivers-dvb-usb-it913x \
    enigma2-plugin-drivers-dvb-usb-pctv452e \
"


export IMAGE_BASENAME = "venton-image"
IMAGE_LINGUAS = ""

inherit image

rootfs_postprocess() {
    curdir=$PWD
    cd ${IMAGE_ROOTFS}

    # because we're so used to it
    ln -s opkg usr/bin/ipkg || true
    ln -s opkg-cl usr/bin/ipkg-cl || true

    cd $curdir
}

ROOTFS_POSTPROCESS_COMMAND += "rootfs_postprocess; "

export NFO = '${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.nfo'

generate_nfo() {
    VER=`grep Version: "${IMAGE_ROOTFS}/usr/lib/ipkg/info/enigma2.control" | cut -b 10-26`
    echo "Enigma2: ${VER}" > ${NFO}
    echo "Machine: ${MACHINE}" >> ${NFO}
    DATE=`date +%Y-%m-%d' '%H':'%M`
    echo "Date: ${DATE}" >> ${NFO}
    echo "Issuer: Sezam Support" >> ${NFO}
    echo "Link: ${DISTRO_FEED_URI}" >> ${NFO}
    if [ "${DESC}" != "" ]; then
            echo "Description: ${DESC}" >> ${NFO}
            echo "${DESC}" >> ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.desc
    fi
    MD5SUM=`md5sum ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.nfi | cut -b 1-32`
    echo "MD5: ${MD5SUM}" >> ${NFO}
}

do_rootfs_append() {
    generate_nfo
}
