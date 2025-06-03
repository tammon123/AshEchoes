let realRate = "1_0.8_0.6_0.5_0.5"
export function compute(memorys, types, data, t1c, t2c, t3c, t4c, t5c, computeRate) {
	if (computeRate.split("_").length >= 1) {
		realRate = computeRate
	}
	let rTypes = []
	let rMemorys = []
	for (let j = 0; j < types.length; j++) {
		if (types[j] != 0) {
			rTypes.push(types[j] - 1)
		}
	}
	for (let j of memorys) {
		if (j.count == 0) {
			continue
		}
		let e = {
			...j
		}
		let value = []
		for (let i of rTypes) {
			value.push(parseInt(e.value.split(',')[parseInt(i)]))
		}
		e.cvalue = value
		rMemorys.push(e)
	}
	console.log(rTypes)
	if (rTypes.length == 1) {
		singleCompute(rMemorys, data, t1c, t2c, t3c, t4c, t5c)
		return
	}
	if (rTypes.length == 2) {
		c2Compute(rMemorys, data, t1c, t2c, t3c, t4c, t5c)
		return
	}
	if (rTypes.length == 3) {
		c3Compute(rMemorys, data, t1c, t2c, t3c, t4c, t5c)
		return
	}
	if (rTypes.length == 4) {
		c4Compute(rMemorys, data, t1c, t2c, t3c, t4c, t5c)
		return
	}
	if (rTypes.length == 5) {
		c5Compute(rMemorys, data, t1c, t2c, t3c, t4c, t5c)
		return
	}
}

function singleCompute(rMemorys, datac, t1c, t2c, t3c, t4c, t5c) {
	const map = rMemorys.reduce((acc, obj) => {
		// 如果acc中还没有当前对象的category，则初始化一个新的数组
		if (!acc.has(obj.category)) {
			acc.set(obj.category, []);
		}
		// 先将对象添加到对应category的数组中
		acc.get(obj.category).push(obj);

		// 然后对该category下的对象按照id降序排序
		acc.get(obj.category).sort((a, b) => computeRank(b, 0) - computeRank(a, 0));

		return acc;
	}, new Map());
	packData(map, datac, t1c, t2c, t3c, t4c, t5c)
}

function c2Compute(rMemorys, datac, t1c, t2c, t3c, t4c, t5c) {
	const map = rMemorys.reduce((acc, obj) => {
		// 如果acc中还没有当前对象的category，则初始化一个新的数组
		if (!acc.has(obj.category)) {
			acc.set(obj.category, []);
		}
		// 先将对象添加到对应category的数组中
		acc.get(obj.category).push(obj);
		// 然后对该category下的对象按照id降序排序
		acc.get(obj.category).sort((a, b) => (computeRank(b, 0) + computeRank(b, 1)) - (computeRank(a, 0) +
			computeRank(a, 1)));
		return acc;
	}, new Map());

	packData(map, datac, t1c, t2c, t3c, t4c, t5c)
}

function c3Compute(rMemorys, datac, t1c, t2c, t3c, t4c, t5c) {
	const map = rMemorys.reduce((acc, obj) => {
		// 如果acc中还没有当前对象的category，则初始化一个新的数组
		if (!acc.has(obj.category)) {
			acc.set(obj.category, []);
		}
		// 先将对象添加到对应category的数组中
		acc.get(obj.category).push(obj);
		// 然后对该category下的对象按照id降序排序
		acc.get(obj.category).sort((a, b) => (computeRank(b, 0) + computeRank(b, 1) + computeRank(b, 2)) - (
			computeRank(a, 0) + computeRank(a, 1) + computeRank(a, 2)));
		return acc;
	}, new Map());

	packData(map, datac, t1c, t2c, t3c, t4c, t5c)
}

function c4Compute(rMemorys, datac, t1c, t2c, t3c, t4c, t5c) {
	const map = rMemorys.reduce((acc, obj) => {
		// 如果acc中还没有当前对象的category，则初始化一个新的数组
		if (!acc.has(obj.category)) {
			acc.set(obj.category, []);
		}
		// 先将对象添加到对应category的数组中
		acc.get(obj.category).push(obj);
		// 然后对该category下的对象按照id降序排序
		acc.get(obj.category).sort((a, b) => (computeRank(b, 0) + computeRank(b, 1) + computeRank(b, 2) +
			computeRank(b, 3)) - (
			computeRank(a, 0) + computeRank(a, 1) + computeRank(a, 2) + computeRank(a, 3)));
		return acc;
	}, new Map());

	packData(map, datac, t1c, t2c, t3c, t4c, t5c)
}



function c5Compute(rMemorys, datac, t1c, t2c, t3c, t4c, t5c) {
	const map = rMemorys.reduce((acc, obj) => {
		// 如果acc中还没有当前对象的category，则初始化一个新的数组
		if (!acc.has(obj.category)) {
			acc.set(obj.category, []);
		}
		// 先将对象添加到对应category的数组中
		acc.get(obj.category).push(obj);
		// 然后对该category下的对象按照id降序排序
		acc.get(obj.category).sort((a, b) => (computeRank(b, 0) + computeRank(b, 1) + computeRank(b, 2) +
			computeRank(b, 3) + computeRank(b, 4)) - (
			computeRank(a, 0) + computeRank(a, 1) + computeRank(a, 2) + computeRank(a, 3) + computeRank(
				a, 4)));
		return acc;
	}, new Map());

	packData(map, datac, t1c, t2c, t3c, t4c, t5c)
}

function packData(map, datac, t1c, t2c, t3c, t4c, t5c) {
	const datav = {
		'体质': 0,
		'防御': 0,
		'攻击': 0,
		'专精': 0,
		'终端': 0,
	}
	let allArr = [
		...map.get('体质').slice(0, 3),
		...map.get('防御').slice(0, 3),
		...map.get('攻击').slice(0, 3),
		...map.get('专精').slice(0, 3),
		...map.get('终端').slice(0, 3),
	]
	const category = ['体质', '防御', '攻击', '专精', '终端']
	for (let cate of category) {
		let valueIndex = -1
		if (cate == '体质') valueIndex = 0
		if (cate == '防御') valueIndex = 1
		if (cate == '攻击') valueIndex = 2
		if (cate == '专精') valueIndex = 3
		if (cate == '终端') valueIndex = 4
		let value = 0
		for (let i in allArr) {
			let rate = 1
			let item = allArr[i]
			let vvalue = parseInt(item.value.split(',')[valueIndex])
			let svalue = 0
			if (i % 3 == 0) {
				rate = 10
			}
			if (item.rank == "R") {
				svalue = (0.6 * vvalue) * rate
			}
			if (item.rank == "SR") {
				svalue = (vvalue * (0.68 + item.count * 0.02)) * rate
			}
			if (item.rank == "SSR") {
				svalue = (vvalue * (0.76 + item.count * 0.04)) * rate
			}
			svalue = Math.trunc(svalue)
			value += svalue
		}
		value = Math.trunc(value)
		if (value > 940) {
			let c = Math.trunc((value - 940) / 2)
			// 30是送的 实际是60/2
			value = 1000 + c
		} else {
			if (value <= 600) {
				value = Math.trunc(1.1 * 600)
			} else {
				value = Math.trunc(1.1 * 600) + (value - 600)
			}
		}
		datav[category[valueIndex]] = value
	}
	t1c(map.get('体质').slice(0, 3))
	t2c(map.get('防御').slice(0, 3))
	t3c(map.get('攻击').slice(0, 3))
	t4c(map.get('专精').slice(0, 3))
	t5c(map.get('终端').slice(0, 3))
	datac(datav)
}


function computeRank(item, index) {
	let svalue = 0
	const rates = realRate.split("_")
	let rate = rates[index]

	let vvalue = parseInt(item.cvalue[index])
	if (item.rank == "R") {
		svalue = (0.6 * vvalue) * rate
	}
	if (item.rank == "SR") {
		svalue = (vvalue * (0.68 + item.count * 0.02)) * rate
	}
	if (item.rank == "SSR") {
		svalue = (vvalue * (0.76 + item.count * 0.04)) * rate
	}
	return svalue
}