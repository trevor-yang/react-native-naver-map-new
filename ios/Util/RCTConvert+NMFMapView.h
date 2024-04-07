//
//  RCTConvert+NMFMapView.h
//  mj-studio-react-native-naver-map
//
//  Created by mj on 4/3/24.
//

#import <React/RCTConvert.h>

#import <NMapsMap/NMFCameraPosition.h>
#import <NMapsMap/NMFCameraUpdate.h>
#import <NMapsMap/NMFMapView.h>
#import <NMapsMap/NMFOverlay.h>
#import <NMapsGeometry/NMGLatLng.h>
#import "RNCNaverMapRect.h"
#import "RNCNaverMapRegion.h"

@interface RCTConvert (NMFMapView)

+ (NMFMapType)NMFMapType:(id)json;
+ (NMGLatLng*)NMGLatLng:(id)json;
+ (NMFAlignType*)NMFAlignType:(id)json;
+ (NMFLogoAlign)NMFLogoAlign:(id)json;
+ (RNCNaverMapRegion*)RNCNaverMapRegion:(id)json;
+ (RNCNaverMapRect*)RNCNaverMapRect:(id)json;

@end