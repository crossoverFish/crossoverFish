package com.tyu.web.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.tyu.api.IUserInfoApi;
import com.tyu.common.anno.AccessToken;
import com.tyu.common.anno.Sign;
import com.tyu.common.enums.NotifyEnum;
import com.tyu.common.exception.BusinessException;
import com.tyu.common.exception.NotFoundException;
import com.tyu.common.util.RedisUtilExpired;
import com.tyu.core.model.Demo;
import com.tyu.core.model.UserInfo;
import com.tyu.web.core.factory.NotifyServiceFactory;
import com.tyu.web.service.INotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 测试接口 - controller类
 *
 * @author crossoverFish
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping(value = "/tests")
public class TestController {


	@Autowired
	private NotifyServiceFactory notifyServiceFactory;

	List<Demo> demos = new ArrayList<>();

	@ApiOperation(value = "新增一个Demo类", notes = "新增资源成功返回201, 并给出新增资源地址")
	@PostMapping
	public ResponseEntity<Demo> create(@Validated @RequestBody(required = true) Demo demo) {
		demos.add(demo);
		return ResponseEntity.created(URI.create("http://localhost:8081/tests/" + demo.getId())).body(demo);
	}

	@ApiOperation(value = "根据Demo编号删除Demo信息", notes = "删除资源成功返回204,资源若不存在则响应404")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable(name = "id", required = true) Long id) {
		validateId(id);
		validateNull();
		boolean removed = demos.removeIf(o -> o.getId().equals(id));
		if (removed) {
			return ResponseEntity.noContent().build();
		}
		throw new NotFoundException();
	}

	@ApiOperation(value = "全量更新Demo信息", notes = "更新资源成功返回204,资源与客户端请求参数体未发生改变,资源若不存在则响应404")
	@PutMapping
	public ResponseEntity<Void> update(@Validated @RequestBody(required = true) Demo demo) {
		for (Demo o : demos) {
			if (o.getId().equals(demo.getId())) {
				o.setName(demo.getName());
				o.setNickName(demo.getNickName());
				return ResponseEntity.noContent().build();
			}
		}
		throw new NotFoundException();
	}
	
	@ApiOperation(value = "部分更新Demo信息-更新昵称", notes = "更新资源成功返回200,资源若不存在则响应404")
	@PatchMapping("/{id}")
	public ResponseEntity<Demo> update(@PathVariable(name = "id", required = true) Long id,
			@RequestParam(name = "nickName", required = true) String nickName) {
		validateId(id);
		if (StringUtils.isEmpty(nickName)) {
			throw new BusinessException("昵称不能为空");
		}
		for (Demo o : demos) {
			if (o.getId().equals(id)) {
				o.setNickName(nickName);
				return ResponseEntity.ok(o);
			}
		}
		throw new NotFoundException();
	}



	@ApiOperation(value = "获取Demo列表", notes = "列表资源有数据响应200,无数据则响应204")
	@GetMapping
	public ResponseEntity<List<Demo>> getList() {
		return ResponseEntity.status((demos == null || demos.isEmpty()) ? HttpStatus.NO_CONTENT : HttpStatus.OK)
				.body(demos);
	}



	@ApiOperation(value = "token测试")
	@GetMapping("/token")
	@AccessToken
	public ResponseEntity<UserInfo> testToken(@ApiIgnore UserInfo user) {
		return ResponseEntity.ok(user);
	}

	@ApiOperation(value = "sign测试")
	@PostMapping("/sign")
	@AccessToken
	@Sign
	public ResponseEntity<UserInfo> testSign(@RequestBody UserInfo user) {
		return ResponseEntity.ok(user);
	}

	private void validateId(Long id) {
		if (StringUtils.isEmpty(id)) {
			throw new BusinessException("编号不能为空");
		}
	}

	private void validateNull() {
		if (demos == null || demos.isEmpty()) {
			throw new BusinessException("数据异常,列表为空");
		}
	}




	@ApiOperation(value = "异步通知", notes = "获取异步通知")
	@GetMapping("/getNotifyService/{commandType}")
	public ResponseEntity<String> getNotifyService(@PathVariable(name = "commandType", required = true)String commandType){

		INotifyService notifyService = notifyServiceFactory.getNotifyService(NotifyEnum.buildFromNotifyType(commandType));
		return ResponseEntity.ok(notifyService.toString());
	}


	@ApiOperation(value = "异步通知", notes = "获取异步通知")
	@GetMapping("/testRedis")
	public ResponseEntity<String> testRedis(){
		return ResponseEntity.ok(RedisUtilExpired.StringOps.get("a"));
	}

	@Autowired
	private IUserInfoApi userInfoApi;

	@GetMapping("/testTransaction")
	public void testTransaction(){
		userInfoApi.testTransaction();
	}
}
